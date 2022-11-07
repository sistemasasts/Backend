package com.isacore.quality.controller;

import com.isacore.quality.model.spp.*;
import com.isacore.quality.service.spp.ISolicitudPPInformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/solicitudPPInforme")
public class SolicitudPPInformeControlador {

    @Autowired
    private ISolicitudPPInformeService servicio;

    @GetMapping("/{solicitudId}")
    public ResponseEntity<SolicitudPruebaProcesoInforme> obtenerInformePorSolicitudPPId(@PathVariable("solicitudId") long solicitudId) {
        SolicitudPruebaProcesoInforme solicitudes = servicio.buscarPorSolicitudId(solicitudId);
        return ResponseEntity.ok(solicitudes);
    }

    @PutMapping("/produccion")
    public ResponseEntity<SolicitudPruebaProcesoInforme> actualizarSolicitudPPInforme(@RequestBody SolicitudPruebaProcesoInforme informe) {
        SolicitudPruebaProcesoInforme solicitudes = servicio.modificar(informe);
        return ResponseEntity.ok(solicitudes);
    }

    @PutMapping("/mantenimiento")
    public ResponseEntity<SolicitudPruebaProcesoInforme> actualizarSolicitudPPInformeMantenimiento(@RequestBody SolicitudPruebaProcesoInforme informe) {
        SolicitudPruebaProcesoInforme solicitudes = servicio.modificarMantenimiento(informe);
        return ResponseEntity.ok(solicitudes);
    }

    @PutMapping("/calidad")
    public ResponseEntity<SolicitudPruebaProcesoInforme> actualizarSolicitudPPInformeCalidad(@RequestBody SolicitudPruebaProcesoInforme informe) {
        SolicitudPruebaProcesoInforme solicitudes = servicio.modificarCalidad(informe);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/agregarMaterialUtilizado/{solicitudId}")
    public ResponseEntity<List<MaterialUtilizado>> agregarMaterialUtilizado(@PathVariable("solicitudId") long solicitudId, @RequestBody MaterialUtilizado materialUtilizado) {
        List<MaterialUtilizado> resultadoConsulta = this.servicio.agregarMaterialUtilizado(solicitudId, materialUtilizado);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @PostMapping("/editarMaterialUtilizado/{solicitudId}")
    public ResponseEntity<List<MaterialUtilizado>> editarMaterialFormula(@PathVariable("solicitudId") long solicitudId, @RequestBody MaterialUtilizado materialUtilizado) {
        List<MaterialUtilizado> resultadoConsulta = this.servicio.modificarMaterialUtilizado(solicitudId, materialUtilizado);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @DeleteMapping("/eliminarMaterialUtilizado/{solicitudId}/{materialUtilizadoId}")
    public ResponseEntity<List<MaterialUtilizado>> eliminarMaterialUtilizado(@PathVariable("solicitudId") long solicitudId, @PathVariable("materialUtilizadoId") long materialUtilizadoId) {
        List<MaterialUtilizado> resultadoConsulta = this.servicio.eliminarMaterialUtilizado(solicitudId, materialUtilizadoId);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @PostMapping("/agregarCondicionOperacion/{solicitudId}")
    public ResponseEntity<List<CondicionOperacion>> agregarCondicionOperacion(@PathVariable("solicitudId") long solicitudId, @RequestBody CondicionOperacion condicionOperacion) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.agregarCondicionOperacion(solicitudId, condicionOperacion);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @PostMapping("/editarCondicionOperacion/{solicitudId}")
    public ResponseEntity<List<CondicionOperacion>> editarCondicionOperacion(@PathVariable("solicitudId") long solicitudId, @RequestBody CondicionOperacion condicionOperacion) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.modificarCondicionOperacion(solicitudId, condicionOperacion);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @DeleteMapping("/eliminarCondicionOperacion/{solicitudId}/{condicionOperacionId}/{tipo}")
    public ResponseEntity<List<CondicionOperacion>> eliminarCondicionOperacion(@PathVariable("solicitudId") long solicitudId, @PathVariable("condicionOperacionId") long condicionOperacionId, @PathVariable("tipo") CondicionOperacionTipo tipo) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.eliminarCondicionOperacion(solicitudId, condicionOperacionId, tipo);
        return ResponseEntity.ok(resultadoConsulta);
    }


    @PostMapping("/agregarCondicion/{solicitudId}")
    public ResponseEntity<List<CondicionOperacion>> agregarCondicion(@PathVariable("solicitudId") long solicitudId, @RequestBody Condicion condicion) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.agregarCondicion(solicitudId, condicion);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @PostMapping("/editarCondicion/{solicitudId}")
    public ResponseEntity<List<CondicionOperacion>> editarCondicion(@PathVariable("solicitudId") long solicitudId, @RequestBody Condicion condicion) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.modificarCondicion(solicitudId, condicion);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @DeleteMapping("/eliminarCondicion/{solicitudId}/{condicionOperacionId}/{condicionId}/{tipo}")
    public ResponseEntity<List<CondicionOperacion>> eliminarCondicion(@PathVariable("solicitudId") long solicitudId, @PathVariable("condicionOperacionId") long condicionOperacionId, @PathVariable("condicionId") long condicionId, @PathVariable("tipo") CondicionOperacionTipo tipo) {
        List<CondicionOperacion> resultadoConsulta = this.servicio.eliminarCondicion(solicitudId, condicionOperacionId, condicionId, tipo);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping(value="/reporte/{solicitudPPId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarReporte(@PathVariable("solicitudPPId") long solicitudPPId) {
        byte [] data = null;
        data = this.servicio.generateReporte(solicitudPPId);
        return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
    }
}
