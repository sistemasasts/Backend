package com.isacore.quality.service.impl.pnc;

import com.isacore.notificacion.servicio.ServicioNotificacionPnc;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.mapper.pnc.PncSalidaMaterialMapper;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.pnc.*;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.IUnidadMedidadRepo;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.pnc.IPncDefectoRepo;
import com.isacore.quality.repository.pnc.IPncSalidaMaterialInfoAddRepo;
import com.isacore.quality.repository.pnc.IPncSalidaMaterialRepo;
import com.isacore.quality.repository.pnc.IProductoNoConformeRepo;
import com.isacore.quality.service.pnc.IPncHistorialService;
import com.isacore.quality.service.pnc.IPncPlanAccionService;
import com.isacore.quality.service.pnc.IPncSalidaMaterialService;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PncSalidaMaterialServiceImpl implements IPncSalidaMaterialService {

    private final IPncSalidaMaterialRepo repositorio;
    private final IProductoNoConformeRepo pncRepositorio;
    private final PncSalidaMaterialMapper mapper;
    private final IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;
    private final IPncHistorialService historialService;
    private final IPncPlanAccionService planAccionService;
    private final ServicioNotificacionPnc notificacionPnc;
    private final IPncDefectoRepo pncDefectoRepositorio;
    private final ValidadorPncFinalizar validadorPncFinalizar;
    private final IPncSalidaMaterialInfoAddRepo pncSalidaMaterialInfoAddRepo;
    private final IUnidadMedidadRepo unidadMedidadRepo;


    @Override
    public PncSalidaMaterialDto registrar(PncSalidaMaterialDto dto) {
        ProductoNoConforme pnc = this.buscarProductoNoConformePorId(dto.getIdPnc());
        PncDefecto defecto = this.buscarDefectoPorId(dto.getIdPncDefecto());
        if (defecto.getSaldo().compareTo(dto.getCantidad()) < 0)
            throw new PncErrorException("Cantidad de salida mayor a stock.");

        PncSalidaMaterial salidaMaterial = new PncSalidaMaterial(
                dto.getFecha(),
                dto.getCantidad(),
                dto.getDestino(),
                pnc,
                dto.getObservacion(),
                UtilidadesSeguridad.nombreUsuarioEnSesion(),
                defecto
        );
        this.repositorio.save(salidaMaterial);
        log.info(String.format("PNC %s -> salida de material regsitrado %s", pnc.getNumero(), salidaMaterial));
        return this.mapper.mapToDto(salidaMaterial);
    }

    @Transactional
    @Override
    public PncSalidaMaterialDto actualizar(PncSalidaMaterialDto dto) {
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        salidaMaterial.setCantidad(dto.getCantidad());
        salidaMaterial.setFecha(dto.getFecha());
        salidaMaterial.setObservacion(dto.getObservacion());

        if (!dto.getDestino().equals(salidaMaterial.getDestino())) {
            List<TipoDestino> destinos = Arrays.asList(TipoDestino.REPROCESO, TipoDestino.RETRABAJO);
            if (destinos.contains(salidaMaterial.getDestino()) && (!destinos.contains(dto.getDestino()))) {
                this.planAccionService.eliminarPorSalidaMaterialId(salidaMaterial.getId());
            }
        }
        salidaMaterial.setDestino(dto.getDestino());
        PncDefecto def = new PncDefecto();
        def.setId(dto.getIdPncDefecto());
        salidaMaterial.setPncDefecto(def);
        log.info(String.format("PNC %s -> salida de material actualizado %s",
                salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial));
        return this.mapper.mapToDto(salidaMaterial);
    }

    @Transactional(readOnly = true)
    @Override
    public PncSalidaMaterialDto listarPorId(long id) {
        PncSalidaMaterial salida = this.buscarPorId(id);
        return this.mapper.mapToDto(salida);
    }

    @Transactional(readOnly = true)
    @Override
    public PncSalidaMaterial listarPorIdCompleto(long id) {
        PncSalidaMaterial salida = this.buscarPorId(id);
        salida.setVerPlanesAccion(salida.verPlanesAccion());
        return salida;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncSalidaMaterialDto> listarPorPncId(long pncId) {
        List<PncSalidaMaterial> salidas = this.repositorio.findByProductoNoConforme_Id(pncId);
        return this.mapper.map(salidas);
    }

    @Transactional
    @Override
    public void enviarAprobacion(PncSalidaMaterialDto dto) {
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        this.validarStockDisponible(salidaMaterial);
        Optional<ConfiguracionGeneralFlujo> configuracion = this.configuracionGeneralFlujoRepo
                .findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud.SALIDA_MATERIAL, NombreConfiguracionFlujo.APROBADOR_SALIDA_MATERIAL);
        if (!configuracion.isPresent())
            throw new PncErrorException(String.format("Configuración %s no encontrada", NombreConfiguracionFlujo.APROBADOR_SALIDA_MATERIAL.getDescripcion()));
        String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacionFlujo()) ? "SALIDA DE MATERIAL ENVIADA" :
                dto.getObservacionFlujo();
        this.historialService.agregar(salidaMaterial, PncOrdenFlujo.INGRESO_SALIDA_MATERIAL, observacion);
        salidaMaterial.marcarComoEnviada(configuracion.get().getValorConfiguracion());
        salidaMaterial.getProductoNoConforme().cambiarEnProceso();
        log.info(String.format("PNC %s -> Salida de material %s enviada a aprobar", salidaMaterial.getProductoNoConforme().getNumero(),
                salidaMaterial));
        try {
            this.notificacionPnc.notificarIngresoSalidaMaterial(salidaMaterial, observacion, this.planAccionService.listarPorSalidaMaterialId(salidaMaterial.getId()));
        } catch (Exception e) {
            log.error(String.format("Error al notificar INGRESO SALIDA DE MATERIAL: %s", e));
        }
    }

    @Transactional
    @Override
    public void regresar(PncSalidaMaterialDto dto) {
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        String observacionRechazado = String.format("REGRESADO::: %s", dto.getObservacionFlujo());
        this.historialService.agregar(salidaMaterial, PncOrdenFlujo.APROBACION, observacionRechazado);
        salidaMaterial.setEstado(EstadoSalidaMaterial.CREADO);
        log.info(String.format("PNC %s -> Salida material %s regresada", salidaMaterial.getProductoNoConforme().getNumero(),
                salidaMaterial.getId()));
        try {
            this.notificacionPnc.notificarSalidaMaterialEstado(salidaMaterial, dto.getObservacionFlujo(),
                    this.planAccionService.listarPorSalidaMaterialId(salidaMaterial.getId()));
        } catch (Exception e) {
            log.error(String.format("Error al notificar ESTADO SALIDA DE MATERIAL: %s", e));
        }
    }

    @Transactional
    @Override
    public void aprobarSalidaMaterial(PncSalidaMaterialDto dto) {
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        salidaMaterial.setFechaAprobacion(LocalDateTime.now());
        if (dto.isAprobado()) {
            String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacionFlujo()) ? "SALIDA DE MATERIAL APROBADA" : dto.getObservacionFlujo();
            this.historialService.agregar(salidaMaterial, PncOrdenFlujo.APROBACION, observacion);
            this.aprobar(salidaMaterial);
        } else {
            String observacionRechazado = String.format("RECHAZADO::: %s", dto.getObservacionFlujo());
            this.historialService.agregar(salidaMaterial, PncOrdenFlujo.APROBACION, observacionRechazado);
            this.noAprobar(salidaMaterial, observacionRechazado);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncSalidaMaterialDto> listarPorEstado(EstadoSalidaMaterial estadoSalidaMaterial) {
        String usuarioSesion = UtilidadesSeguridad.nombreUsuarioEnSesion();
        List<PncSalidaMaterial> salidaMateriales = this.repositorio.findByEstadoIn(Arrays.asList(estadoSalidaMaterial))
                .stream()
                .filter(x -> x.getUsuarioAprobador().equals(usuarioSesion))
                .collect(Collectors.toList());
        return this.mapper.map(salidaMateriales);
    }

    @Transactional
    @Override
    public List<PncSalidaMaterialDto> eliminar(long pncId, long id) {
        this.planAccionService.eliminarPorSalidaMaterialId(id);
        this.repositorio.deleteById(id);
        log.info(String.format("Salida de Material id=%s eliminado", id));
        return this.listarPorPncId(pncId);
    }

    @Transactional
    @Override
    public PncSalidaMaterialInfoAdd actualizarInfoAdd(PncSalidaMaterialInfoAdd dto) {
        PncSalidaMaterialInfoAdd infoAdd = pncSalidaMaterialInfoAddRepo.findById(dto.getId())
                .orElseThrow(()-> new PncErrorException("Salida de material no encontrada"));
        infoAdd.setCantidad(dto.getCantidad());
        infoAdd.setFechaRegistro(LocalDateTime.now());
        infoAdd.setLote(dto.getLote());
        infoAdd.setLoteOriginal(dto.getLoteOriginal());
        infoAdd.setLoteFin(dto.getLoteFin());
        if(dto.getUnidadMedidaId() > 0){
            UnidadMedida unidadMedida = unidadMedidadRepo.findById(dto.getUnidadMedidaId())
                    .orElseThrow(()-> new PncErrorException("Unidad de medidad no encontrada"));
            infoAdd.setUnidadMedida(unidadMedida);
        }
        return infoAdd;
    }

    private void aprobar(PncSalidaMaterial salidaMaterial) {
        List<TipoDestino> destinos = Arrays.asList(TipoDestino.DESPERDICIO, TipoDestino.DONACION, TipoDestino.SALIDA_CONCESION);
        this.validarStockDisponible(salidaMaterial);
        salidaMaterial.getPncDefecto().reducirStock(salidaMaterial.getCantidad());
        if (destinos.contains(salidaMaterial.getDestino())) {
            salidaMaterial.marcarComoCerrada();
            log.info(String.format("PNC %s -> Salida material %s aprobada y cerrada", salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId()));
        } else {
            salidaMaterial.marcarComoAprobada();
            planAccionService.iniciarGestionPlanes(salidaMaterial.getId());
            iniciarInformacionAdicional(salidaMaterial);
            log.info(String.format("PNC %s -> Salida material %s aprobada", salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId()));
        }
        validadorPncFinalizar.verificarSiFinalizaPNC(salidaMaterial.getProductoNoConforme());
    }

    private void noAprobar(PncSalidaMaterial salidaMaterial, String observacion) {
        salidaMaterial.marcarComoNoAprobada();
        try {
            this.notificacionPnc.notificarSalidaMaterialEstado(salidaMaterial, observacion, this.planAccionService.listarPorSalidaMaterialId(salidaMaterial.getId()));
        } catch (Exception e) {
            log.error(String.format("Error al notificar ESTADO SALIDA DE MATERIAL: %s", e));
        }
        log.info(String.format("PNC %s -> Salida material %s no aprobada", salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId()));
    }

    private void validarStockDisponible(PncSalidaMaterial salidaMaterial) {
        if (!salidaMaterial.tieneCantidadDisponible())
            throw new PncErrorException("No existe suficiente catidad para procesar la salida de material");
    }

    private PncSalidaMaterial buscarPorId(long id) {
        Optional<PncSalidaMaterial> pnc = this.repositorio.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("Salida de material no encontrada");
        return pnc.get();
    }

    private ProductoNoConforme buscarProductoNoConformePorId(long id) {
        Optional<ProductoNoConforme> pnc = this.pncRepositorio.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("PNC no encontrado");
        return pnc.get();
    }

    private PncDefecto buscarDefectoPorId(long id) {
        Optional<PncDefecto> pnc = this.pncDefectoRepositorio.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("Defecto no encontrado");
        return pnc.get();
    }

    private void iniciarInformacionAdicional(PncSalidaMaterial pncSalidaMaterial) {
        List<PncSalidaMaterialInfoAdd> infoAdd = new ArrayList<>();
        List<PncPlanAccionDto> planes = planAccionService.listarPorSalidaMaterialId(pncSalidaMaterial.getId());
        if (planes.stream().anyMatch(PncPlanAccionDto::isLlenarInfoAdicional)) {
            TipoDestino destino = pncSalidaMaterial.getDestino();
            PncPlanAccionDto plan = planes.stream().filter(PncPlanAccionDto::isLlenarInfoAdicional).findFirst().get();
            for (TipoInfoAdd tipoInfoAdd : Arrays.stream(TipoInfoAdd.values())
                    .filter(x -> x.getGrupo() == destino.getGrupoInfoAdicional()).collect(Collectors.toList())) {
                pncSalidaMaterial.agregarInfoAdd(new PncSalidaMaterialInfoAdd(tipoInfoAdd, plan.getResponsable()));
            }
//            pncSalidaMaterial.setInformacionAdicional(infoAdd);
            repositorio.save(pncSalidaMaterial);
        }
    }
}
