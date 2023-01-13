package com.isacore.quality.service.spp;

import com.isacore.quality.model.spp.*;

import java.util.List;

public interface ISolicitudPPInformeService {

    SolicitudPruebaProcesoInforme registrar(SolicitudPruebasProceso solicitud);

    SolicitudPruebaProcesoInforme modificar(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme);
    SolicitudPruebaProcesoInforme modificarMantenimiento(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme);
    SolicitudPruebaProcesoInforme modificarCalidad(SolicitudPruebaProcesoInforme solicitudPruebaProcesoInforme);

    SolicitudPruebaProcesoInforme buscarPorSolicitudId(long solicitudId);

    List<MaterialUtilizado> agregarMaterialUtilizado(long solicitudInformeId, MaterialUtilizado material);
    List<MaterialUtilizado> modificarMaterialUtilizado(long solicitudInformeId ,MaterialUtilizado material);
    List<MaterialUtilizado> eliminarMaterialUtilizado(long solicitudInformeId ,long materialId);

    List<CondicionOperacion> agregarCondicionOperacion(long solicitudInformeId, CondicionOperacion condicionOperacion);
    List<CondicionOperacion> modificarCondicionOperacion(long solicitudInformeId ,CondicionOperacion condicionOperacion);
    List<CondicionOperacion> eliminarCondicionOperacion(long solicitudInformeId ,long condicionOperacionId, CondicionOperacionTipo tipo);

    List<CondicionOperacion> agregarCondicion(long solicitudInformeId, Condicion condicion);
    List<CondicionOperacion> modificarCondicion(long solicitudInformeId ,Condicion condicion);
    List<CondicionOperacion> eliminarCondicion(long solicitudInformeId,long condicionOperacionId ,long condicionId, CondicionOperacionTipo tipo);

    byte[] generateReporte(long solicitudPruebaProcesoId);

    void validarConclusionesMantenimientoDDP05(long solicitudId);
}
