package com.isacore.quality.service.impl.pnc;

import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.mapper.pnc.PncSalidaMaterialMapper;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.pnc.*;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.pnc.IPncSalidaMaterialRepo;
import com.isacore.quality.repository.pnc.IProductoNoConformeRepo;
import com.isacore.quality.service.pnc.IPncHistorialService;
import com.isacore.quality.service.pnc.IPncSalidaMaterialService;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PncSalidaMaterialServiceImpl implements IPncSalidaMaterialService {

    private final IPncSalidaMaterialRepo repositorio;
    private final IProductoNoConformeRepo pncRepositorio;
    private final PncSalidaMaterialMapper mapper;
    private final IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;
    private final IPncHistorialService historialService;


    @Override
    public PncSalidaMaterialDto registrar(PncSalidaMaterialDto dto) {
        ProductoNoConforme pnc = this.buscarProductoNoConformePorId(dto.getIdPnc());
        PncSalidaMaterial salidaMaterial = new PncSalidaMaterial(
                dto.getFecha(),
                dto.getCantidad(),
                dto.getDestino(),
                pnc,
                dto.getObservacion(),
                UtilidadesSeguridad.nombreUsuarioEnSesion()
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
        Optional<ConfiguracionGeneralFlujo> configuracion = this.configuracionGeneralFlujoRepo
                .findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud.SALIDA_MATERIAL, NombreConfiguracionFlujo.APROBADOR_SALIDA_MATERIAL);
        if (!configuracion.isPresent())
            throw new PncErrorException(String.format("Configuración %s no encontrada", NombreConfiguracionFlujo.APROBADOR_SALIDA_MATERIAL.getDescripcion()));
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacionFlujo()) ? "SALIDA DE MATERIAL ENVIADA" :
                dto.getObservacionFlujo();
        this.historialService.agregar(salidaMaterial, PncOrdenFlujo.INGRESO_SALIDA_MATERIAL, observacion);
        salidaMaterial.marcarComoEnviada(configuracion.get().getValorConfiguracion());
        salidaMaterial.getProductoNoConforme().cambiarEnProceso();
        log.info(String.format("PNC %s -> Salida de material %s enviada a aprobar", salidaMaterial.getProductoNoConforme().getNumero(),
                salidaMaterial));
    }

    @Transactional
    @Override
    public void aprobarSalidaMaterial(PncSalidaMaterialDto dto) {
        PncSalidaMaterial salidaMaterial = this.buscarPorId(dto.getId());
        if (dto.isAprobado()) {
            String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacionFlujo()) ? "SALIDA DE MATERIAL APROBADA" : dto.getObservacionFlujo();
            this.historialService.agregar(salidaMaterial, PncOrdenFlujo.APROBACION, observacion);
            this.aprobar(salidaMaterial);
        } else {
            String observacionRechazado = String.format("RECHAZADO::: %s", dto.getObservacionFlujo());
            this.historialService.agregar(salidaMaterial, PncOrdenFlujo.APROBACION, observacionRechazado);
            this.noAprobar(salidaMaterial);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncSalidaMaterialDto> listarPorEstado(EstadoSalidaMaterial estadoSalidaMaterial) {
        List<PncSalidaMaterial> salidaMateriales = this.repositorio.findByEstadoIn(Arrays.asList(estadoSalidaMaterial));
        return this.mapper.map(salidaMateriales);
    }

    private void aprobar(PncSalidaMaterial salidaMaterial) {
        List<TipoDestino> destinos = Arrays.asList(TipoDestino.DESPERDICIO, TipoDestino.DONACION);
        if (destinos.contains(salidaMaterial.getDestino())) {
            this.validarStockDisponible(salidaMaterial);
            salidaMaterial.getProductoNoConforme().reducirStock(salidaMaterial.getCantidad());
            salidaMaterial.marcarComoCerrada();
            log.info(String.format("PNC %s -> Salida material %s aprobada y cerrada", salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId()));
        } else {
            salidaMaterial.marcarComoAprobada();
            log.info(String.format("PNC %s -> Salida material %s aprobada", salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId()));
        }
    }

    private void noAprobar(PncSalidaMaterial salidaMaterial) {
        salidaMaterial.marcarComoNoAprobada();
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
}
