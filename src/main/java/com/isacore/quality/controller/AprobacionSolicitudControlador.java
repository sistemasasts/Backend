package com.isacore.quality.controller;

import com.isacore.quality.dto.SolicitudAprobacionDto;
import com.isacore.quality.service.aprobacionSolicitud.AprobacionSolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/aprobacionSolicitud")
public class AprobacionSolicitudControlador {

    @Autowired
    private AprobacionSolicitudService service;

    @GetMapping(value = "/pendientesValidar")
    public ResponseEntity<List<SolicitudAprobacionDto>> pendientesValidar() {
        List<SolicitudAprobacionDto> data = service.listarPendientesValidar();
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/pendientesAprobarInforme")
    public ResponseEntity<List<SolicitudAprobacionDto>> pendientesAprobarInforme() {
        List<SolicitudAprobacionDto> data = service.listarPendientesRevisarInforme();
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/pendientesAprobarSolicitud")
    public ResponseEntity<List<SolicitudAprobacionDto>> pendientesAprobarSolicitud() {
        List<SolicitudAprobacionDto> data = service.listarPendientesAprobarSolicitud();
        return ResponseEntity.ok(data);
    }
}
