package com.isacore.quality.service.impl.pnc;

import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
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
import com.isacore.quality.repository.pnc.*;
import com.isacore.quality.service.pnc.IPncHistorialService;
import com.isacore.quality.service.pnc.IPncPlanAccionService;
import com.isacore.quality.service.pnc.IPncSalidaMaterialService;
import com.isacore.servicio.reporte.IGeneradorJasperReports;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    private final IPncSalidaConcesionRepo pncSalidaConcesionRepo;
    private final IGeneradorJasperReports reporteServicio;


    @Transactional
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
        if (dto.getDestino().equals(TipoDestino.SALIDA_CONCESION)) {
            agregarSalidaConcesion(dto, salidaMaterial);
        }
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
            if(dto.getDestino().equals(TipoDestino.SALIDA_CONCESION)){
                this.agregarSalidaConcesion(dto, salidaMaterial);
            }
        }
        if(salidaMaterial.getDestino().equals(TipoDestino.SALIDA_CONCESION) && salidaMaterial.getSalidaConcesion() != null){
            salidaMaterial.getSalidaConcesion().setCliente(dto.getCliente());
            salidaMaterial.getSalidaConcesion().setFactura(dto.getFactura());
            salidaMaterial.getSalidaConcesion().setResponsableVenta(dto.getResponsableVenta());
            salidaMaterial.getSalidaConcesion().setResponsableBodega(dto.getResponsableBodega());
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
                .orElseThrow(() -> new PncErrorException("Salida de material no encontrada"));
        infoAdd.setCantidad(dto.getCantidad());
        infoAdd.setFechaRegistro(LocalDateTime.now());
        infoAdd.setLote(dto.getLote());
        infoAdd.setLoteOriginal(dto.getLoteOriginal());
        infoAdd.setLoteFin(dto.getLoteFin());
        if (dto.getUnidadMedidaId() > 0) {
            UnidadMedida unidadMedida = unidadMedidadRepo.findById(dto.getUnidadMedidaId())
                    .orElseThrow(() -> new PncErrorException("Unidad de medidad no encontrada"));
            infoAdd.setUnidadMedida(unidadMedida);
        }
        return infoAdd;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] generateReporteSalidaConcesion(long id) {
        Optional<PncSalidaMaterial> pncOp = this.repositorio.findById(id);
        if (!pncOp.isPresent())
            throw new PncErrorException("Salida material tipo concesión no encotrasda");
        try {
            return reporteServicio.generarReporte("SalidaConcesion", Collections.singleton(this.crearReporteDTO(pncOp.get())), new HashMap<>());
        } catch (JasperReportsException e) {
            log.error(String.format("Error PNC Reporte: %s", e));
            throw new ReporteExeption("PNC");
        }
    }

    private PncSalidaConcesionReporteDto crearReporteDTO(PncSalidaMaterial salida){
        return new PncSalidaConcesionReporteDto(
                salida.getProductoNoConforme().getProducto().getNameProduct(),
                salida.getSalidaConcesion().getResponsableVenta(),
                salida.getSalidaConcesion().getResponsableBodega(),
                salida.getSalidaConcesion().getCliente(),
                salida.getFecha(),
                salida.getCantidad(),
                salida.getPncDefecto().getValidez(),
                salida.getProductoNoConforme().getLote(),
                salida.getSalidaConcesion().getFactura(),
                String.valueOf(salida.getProductoNoConforme().getNumero())
        );
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
                    .filter(x -> !x.equals(TipoInfoAdd.PRODUCTO_RECUPERADO))
                    .filter(x -> x.getGrupo() == destino.getGrupoInfoAdicional()).collect(Collectors.toList())) {
                pncSalidaMaterial.agregarInfoAdd(new PncSalidaMaterialInfoAdd(tipoInfoAdd, plan.getResponsable()));
            }
//            pncSalidaMaterial.setInformacionAdicional(infoAdd);
            repositorio.save(pncSalidaMaterial);
        }
    }

    private void agregarSalidaConcesion(PncSalidaMaterialDto dto, PncSalidaMaterial salidaMaterial) {
        PncSalidaConcesion salidaConcesion = new PncSalidaConcesion(
                dto.getCliente(),
                dto.getFactura(),
                dto.getResponsableVenta(),
                dto.getResponsableBodega());
        this.pncSalidaConcesionRepo.save(salidaConcesion);
        salidaMaterial.setSalidaConcesion(salidaConcesion);
    }
}
