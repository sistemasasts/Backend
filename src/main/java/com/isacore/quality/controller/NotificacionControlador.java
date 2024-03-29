package com.isacore.quality.controller;

import com.isacore.notificacion.servicio.ServicioNotificacionSolicitudPP;
import com.isacore.quality.service.impl.recordatorio.RecordatorioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notificacionesCorreo")
public class NotificacionControlador {

    @Autowired
    private ServicioNotificacionSolicitudPP servicioNotificacionSolicitudPP;

    @Autowired
    private RecordatorioServiceImpl recordatorioService;

    @GetMapping("/prueba/{correo}")
    public ResponseEntity<Object> enviarCorreoPrueba(@PathVariable("correo") String correo) {
        servicioNotificacionSolicitudPP.mensajePrueba(correo);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/enviarRecordatorio")
    public ResponseEntity<Object> enviarRecordatorio() {
        recordatorioService.notificarSolicitudesPendientes();
        return ResponseEntity.ok(true);
    }
}
