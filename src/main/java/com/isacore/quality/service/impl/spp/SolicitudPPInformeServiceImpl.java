package com.isacore.quality.service.impl.spp;

import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
import com.isacore.quality.exception.SolicitudPruebaProcesoErrorException;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.spp.*;
import com.isacore.quality.repository.IUnidadMedidadRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoInformeRepo;
import com.isacore.quality.service.spp.ISolicitudPPInformeService;
import com.isacore.servicio.reporte.IGeneradorJasperReports;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SolicitudPPInformeServiceImpl implements ISolicitudPPInformeService {
    private static final Log LOG = LogFactory.getLog(SolicitudPPInformeServiceImpl.class);
    private ISolicitudPruebaProcesoInformeRepo informeRepo;
    private IUnidadMedidadRepo unidadMedidadRepo;
    private IGeneradorJasperReports reporteServicio;

    @Autowired
    public SolicitudPPInformeServiceImpl(
        ISolicitudPruebaProcesoInformeRepo informeRepo,
        IUnidadMedidadRepo unidadMedidadRepo,
        IGeneradorJasperReports reporteServicio) {
        this.informeRepo = informeRepo;
        this.unidadMedidadRepo = unidadMedidadRepo;
        this.reporteServicio = reporteServicio;
    }

    @Transactional
    @Override
    public SolicitudPruebaProcesoInforme registrar(SolicitudPruebasProceso solicitud) {
        Optional<SolicitudPruebaProcesoInforme> informeExistente = this.informeRepo.findBySolicitudPruebasProceso_Id(solicitud.getId());
        if (!informeExistente.isPresent()) {
            SolicitudPruebaProcesoInforme informe = new SolicitudPruebaProcesoInforme(solicitud);
            this.cargarCondicionesOperacionPorDefecto(informe);
            this.informeRepo.save(informe);

            LOG.info(String.format("Solicitud prueba proceso %s => informe creado %s", solicitud.getCodigo(), informe));
            return informe;
        }
        return informeExistente.get();
    }

    @Transactional
    @Override
    public SolicitudPruebaProcesoInforme modificar(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme) {
        SolicitudPruebaProcesoInforme informe = this.obtenerInforme(solicitudPruebaProcesoInforme.getId());
        informe.setFechaPrueba(solicitudPruebaProcesoInforme.getFechaPrueba());
        informe.setProducto(solicitudPruebaProcesoInforme.getProducto());
        informe.setCantidadProducida(solicitudPruebaProcesoInforme.getCantidadProducida());
        informe.setLote(solicitudPruebaProcesoInforme.getLote());
        informe.setOrdenFabricacion(solicitudPruebaProcesoInforme.getOrdenFabricacion());
        informe.setLineaFabricacion(solicitudPruebaProcesoInforme.getLineaFabricacion());
        informe.setLineaFabricacionUnidad(solicitudPruebaProcesoInforme.getLineaFabricacionUnidad());
        informe.setCantidadProductoTerminado(solicitudPruebaProcesoInforme.getCantidadProductoTerminado());
        informe.setCantidadProductoNoConforme(solicitudPruebaProcesoInforme.getCantidadProductoNoConforme());
        informe.setCantidadDesperdicio(solicitudPruebaProcesoInforme.getCantidadDesperdicio());
        informe.setCantidadProductoPrueba(solicitudPruebaProcesoInforme.getCantidadProductoPrueba());
        informe.setObservacionProduccion(solicitudPruebaProcesoInforme.getObservacionProduccion());
        informe.setUnidadProductoTerminado(solicitudPruebaProcesoInforme.getUnidadProductoTerminado());
        informe.setUnidadProductoNoConforme(solicitudPruebaProcesoInforme.getUnidadProductoNoConforme());
        informe.setUnidadDesperdicio(solicitudPruebaProcesoInforme.getUnidadDesperdicio());
        informe.setUnidadProductoPrueba(solicitudPruebaProcesoInforme.getUnidadProductoPrueba());
        return informe;
    }

    @Transactional
    @Override
    public SolicitudPruebaProcesoInforme modificarMantenimiento(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme) {
        SolicitudPruebaProcesoInforme informe = this.obtenerInforme(solicitudPruebaProcesoInforme.getId());
        informe.setObservacionMantenimiento(solicitudPruebaProcesoInforme.getObservacionMantenimiento());
        LOG.info(String.format("Informe DDP05 actualizado mantenimiento %s", solicitudPruebaProcesoInforme));
        return informe;
    }

    @Transactional
    @Override
    public SolicitudPruebaProcesoInforme modificarCalidad(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme) {
        SolicitudPruebaProcesoInforme informe = this.obtenerInforme(solicitudPruebaProcesoInforme.getId());
        informe.setObservacionCalidad(solicitudPruebaProcesoInforme.getObservacionCalidad());
        LOG.info(String.format("Informe DDP05 actualizado calidad %s", solicitudPruebaProcesoInforme));
        return informe;
    }

    @Transactional(readOnly = true)
    @Override
    public SolicitudPruebaProcesoInforme buscarPorSolicitudId(long solicitudId) {
        return this.informeRepo.findBySolicitudPruebasProceso_Id(solicitudId).orElse(null);
    }

    @Transactional
    @Override
    public List<MaterialUtilizado> agregarMaterialUtilizado(long solicitudInformeId, MaterialUtilizado material) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        UnidadMedida unidad = this.buscarUnidadMedida(material.getUnidad().getId());
        solicitudInforme.agregarMaterialUtilizado(new MaterialUtilizado(material.getNombre(), unidad,
            material.getCantidadSolicitada(), material.getCantidadUtilizada()));
        LOG.info(String.format("Material utilizado agregado %s", material));
        return solicitudInforme.getMaterialesUtilizado();
    }

    @Transactional
    @Override
    public List<MaterialUtilizado> modificarMaterialUtilizado(long solicitudInformeId, MaterialUtilizado material) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        Optional<MaterialUtilizado> materialRecargado =
            solicitudInforme.getMaterialesUtilizado().stream().filter(x -> x.getId().compareTo(material.getId()) == 0).findFirst();
        if (materialRecargado.isPresent()) {
            UnidadMedida unidad = this.buscarUnidadMedida(material.getUnidad().getId());
            materialRecargado.get().modificar(material.getNombre(),
                unidad, material.getCantidadSolicitada(), material.getCantidadUtilizada());
            LOG.info(String.format("Material utilizado modificado %s", materialRecargado.get()));
        }
        return solicitudInforme.getMaterialesUtilizado();
    }

    @Transactional
    @Override
    public List<MaterialUtilizado> eliminarMaterialUtilizado(long solicitudInformeId, long materialId) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        solicitudInforme.eliminarMaterialUtilizado(materialId);
        LOG.info(String.format("Maerial Utilizado eliminado id %s", materialId));
        return solicitudInforme.getMaterialesUtilizado();
    }

    @Transactional
    @Override
    public List<CondicionOperacion> agregarCondicionOperacion(long solicitudInformeId, CondicionOperacion condicionOperacion) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        solicitudInforme.agregarCondicionOperacion(new CondicionOperacion(condicionOperacion.getProceso(), condicionOperacion.getObservacion(),
            condicionOperacion.getTipo()));
        LOG.info(String.format("Operacion condicion agregado %s", condicionOperacion));
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(condicionOperacion.getTipo())).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CondicionOperacion> modificarCondicionOperacion(long solicitudInformeId, CondicionOperacion condicionOperacion) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        Optional<CondicionOperacion> condicionOperacionRecargado =
            solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getId().compareTo(condicionOperacion.getId()) == 0).findFirst();
        if (condicionOperacionRecargado.isPresent()) {
            condicionOperacionRecargado.get().setProceso(condicionOperacion.getProceso());
            condicionOperacionRecargado.get().setObservacion(condicionOperacion.getObservacion());
            LOG.info(String.format("Condicion operacion modificado %s", condicionOperacionRecargado.get()));
        }
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(condicionOperacion.getTipo())).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CondicionOperacion> eliminarCondicionOperacion(long solicitudInformeId, long condicionOperacionId, CondicionOperacionTipo tipo) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        solicitudInforme.eliminarOperacionCondicion(condicionOperacionId);
        LOG.info(String.format("Operacion condicion eliminada id %s", condicionOperacionId));
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(tipo)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CondicionOperacion> agregarCondicion(long solicitudInformeId, Condicion condicion) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        CondicionOperacion condicionOperacion = obtenerCondicionOperacionPorId(solicitudInforme, condicion.getCondicionOperacionId());
        UnidadMedida unidad = this.buscarUnidadMedida(condicion.getUnidad().getId());
        condicionOperacion.agregarCondicion(new Condicion(condicion.getMaquinaria(), condicion.getNombre(),
            condicion.getValor(), unidad));
        LOG.info(String.format("Condicion agregado %s", condicion));
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(condicionOperacion.getTipo())).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CondicionOperacion> modificarCondicion(long solicitudInformeId, Condicion condicion) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        CondicionOperacion condicionOperacion = obtenerCondicionOperacionPorId(solicitudInforme, condicion.getCondicionOperacionId());
        Optional<Condicion> condicionRecargado =
            condicionOperacion.getCondiciones().stream().filter(x -> x.getId().compareTo(condicion.getId()) == 0).findFirst();
        if(condicionRecargado.isPresent()){
            UnidadMedida unidad = this.buscarUnidadMedida(condicion.getUnidad().getId());
            condicionRecargado.get().actualizar(condicion.getMaquinaria(), condicion.getNombre(), condicion.getValor(), unidad);
            LOG.info(String.format("Condicion actualizada %s", condicion));
        }
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(condicionOperacion.getTipo())).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<CondicionOperacion> eliminarCondicion(long solicitudInformeId, long condicionOperacionId, long condicionId, CondicionOperacionTipo tipo) {
        SolicitudPruebaProcesoInforme solicitudInforme = this.obtenerInforme(solicitudInformeId);
        CondicionOperacion condicionOperacion = obtenerCondicionOperacionPorId(solicitudInforme, condicionOperacionId);
        condicionOperacion.eliminarCondicion(condicionId);
        LOG.info(String.format("Condicion eliminada id %s", condicionId));
        return solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getTipo().equals(tipo)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] generateReporte(long solicitudPruebaProcesoId) {
        Optional<SolicitudPruebaProcesoInforme> informe = this.informeRepo.findBySolicitudPruebasProceso_Id(solicitudPruebaProcesoId);
        if(!informe.isPresent())
            throw new SolicitudPruebaProcesoErrorException("Informe DDP05 no encontrada");
        try {
            return reporteServicio.generarReporte("DDP05", Collections.singleton(informe.get()), new HashMap<>());
        } catch (JasperReportsException e) {
            LOG.error(String.format("Error DDP05 Reporte: %s", e));
            throw new ReporteExeption("DDP04");
        }
    }

    private SolicitudPruebaProcesoInforme obtenerInforme(long id) {
        Optional<SolicitudPruebaProcesoInforme> solicitudInforme = this.informeRepo.findById(id);
        if (!solicitudInforme.isPresent())
            throw new SolicitudPruebaProcesoErrorException("Solicitud informe no encontrada");
        return solicitudInforme.get();
    }

    private CondicionOperacion obtenerCondicionOperacionPorId(SolicitudPruebaProcesoInforme solicitudInforme, long condicionOperacionId) {
        Optional<CondicionOperacion> condicionOperacionRecargada = solicitudInforme.getCondicionesOperacion().stream().filter(x -> x.getId() == condicionOperacionId).findFirst();
        if (!condicionOperacionRecargada.isPresent())
            throw new SolicitudPruebaProcesoErrorException("Condición operación no encontrada");
        return condicionOperacionRecargada.get();
    }

    private void cargarCondicionesOperacionPorDefecto(SolicitudPruebaProcesoInforme informe){
        List<CondicionOperacion> condiciones = new ArrayList<>();
        condiciones.add(new CondicionOperacion("Polimerización", "N/A", CondicionOperacionTipo.PRODUCCION));
        condiciones.add(new CondicionOperacion("Laminación", "N/A", CondicionOperacionTipo.PRODUCCION));
        condiciones.add(new CondicionOperacion("Mezcla", "N/A", CondicionOperacionTipo.PRODUCCION));
        condiciones.forEach(x -> {
            UnidadMedida unidad1 = this.buscarUnidadMedida("°c");
            if(unidad1 != null)
                x.agregarCondicion(new Condicion(null,"Temperatura", BigDecimal.ZERO,unidad1));
            UnidadMedida unidad2 = this.buscarUnidadMedida("min");
            if(unidad2 != null)
                x.agregarCondicion(new Condicion(null,"Tiempo", BigDecimal.ZERO,unidad2));
            UnidadMedida unidad3 = this.buscarUnidadMedida("rpm");
            if(unidad3 != null)
                x.agregarCondicion(new Condicion(null,"Velocidad", BigDecimal.ZERO,unidad3));
            informe.agregarCondicionOperacion(x);
        });
    }

    private UnidadMedida buscarUnidadMedida(long id){
        UnidadMedida unidad = this.unidadMedidadRepo.findById(id).orElse(null);
        if(unidad == null){
            throw new SolicitudPruebaProcesoErrorException("Unidad de medida no encontrada");
        }
        return unidad;
    }

    private UnidadMedida buscarUnidadMedida(String abreviatura){
        UnidadMedida unidad = this.unidadMedidadRepo.findByAbreviatura(abreviatura).orElse(null);
        return unidad;
    }
}
