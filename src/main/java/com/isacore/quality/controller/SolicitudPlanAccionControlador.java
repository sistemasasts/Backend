package com.isacore.quality.controller;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudPlanAccion.SolicitudPlanAccion;
import com.isacore.quality.service.solicitudPlanAccion.SolicitudPlanAccionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/solicitudPlanesAccion")
public class SolicitudPlanAccionControlador {

    @Autowired
    private SolicitudPlanAccionServiceImpl servicio;

    @PostMapping
    public ResponseEntity<SolicitudPlanAccion> registrar(@RequestBody SolicitudPlanAccion planAccion) {
        SolicitudPlanAccion solicitudPlanAccion = this.servicio.registrar(planAccion);
        return ResponseEntity.ok(solicitudPlanAccion);
    }

    @PutMapping
    public ResponseEntity<SolicitudPlanAccion> modificar(@RequestBody SolicitudPlanAccion planAccion) {
        SolicitudPlanAccion solicitudPlanAccion = this.servicio.modificar(planAccion);
        return ResponseEntity.ok(solicitudPlanAccion);
    }

    @PutMapping("/marcarCumplimiento")
    public ResponseEntity<SolicitudPlanAccion> marcarCumplimiento(@RequestBody SolicitudPlanAccion planAccion) {
        SolicitudPlanAccion solicitudPlanAccion = this.servicio.marcarCumplimiento(planAccion);
        return ResponseEntity.ok(solicitudPlanAccion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable("solicitudId") long solicitudId) {
        boolean resultadoConsulta = this.servicio.eliminar(solicitudId);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping("/{tipo}/{solicitudId}")
    public ResponseEntity<List<SolicitudPlanAccion>> generarReporte(@PathVariable("tipo") TipoSolicitud tipo, @PathVariable("solicitudId") long solicitudId) {
        List<SolicitudPlanAccion> data = this.servicio.listarPorTipoSolicitudYSolicitudId(tipo, solicitudId);
        return ResponseEntity.ok(data);
    }
}
