package com.isacore.quality.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.PrioridadNivel;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.quality.service.se.ISolicitudEnsayoService;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/solicitudesEnsayo")
public class SolicitudEnsayoControlador {

    @Autowired
    private ISolicitudEnsayoService servicio;

    @GetMapping("/nombreSolicitante")
    public ResponseEntity<List<SolicitudEnsayo>> listarSolicitudesPorNombreSolicitante() {
        List<SolicitudEnsayo> solicitudes = servicio.obtenerSolicitudesPorUsuarioSolicitante();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/usuarioGestion")
    public ResponseEntity<List<SolicitudEnsayo>> listarSolicitudesPorUsuarioGestion() {
        List<SolicitudEnsayo> solicitudes = servicio.obtenerSolicitudesPorUsuarioEnGestion();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/usuarioValidador")
    public ResponseEntity<List<SolicitudEnsayo>> listarSolicitudesPorUsuarioValidador() {
        List<SolicitudEnsayo> solicitudes = servicio.obtenerSolicitudesPorUsuarioValidador();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/usuarioAprobador")
    public ResponseEntity<List<SolicitudEnsayo>> listarSolicitudesPorUsuarioAprobador() {
        List<SolicitudEnsayo> solicitudes = servicio.obtenerSolicitudesPorUsuarioAprobador();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudEnsayo> buscarSolicitud(@PathVariable("id") Long id) {
        SolicitudEnsayo solicitudes = servicio.buscarPorId(id);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping
    public ResponseEntity<SolicitudEnsayo> crear(@RequestBody SolicitudEnsayo obj) {
        SolicitudEnsayo solicitud = servicio.create(obj);
        return new ResponseEntity<SolicitudEnsayo>(solicitud, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SolicitudEnsayo> actualizar(@RequestBody SolicitudEnsayo obj) {
        SolicitudEnsayo solicitud = servicio.update(obj);
        return new ResponseEntity<SolicitudEnsayo>(solicitud, HttpStatus.OK);
    }

    @GetMapping("/prioridadesNivel")
    public ResponseEntity<List<CatalogDTO>> listarPrioridad() {
        final List<CatalogDTO> lista = Arrays.asList(PrioridadNivel.ALTO, PrioridadNivel.MEDIO, PrioridadNivel.BAJO).parallelStream().map(x -> {
            return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/enviarSolicitud")
    public ResponseEntity<Object> enviarSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.enviarSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/validarSolicitud")
    public ResponseEntity<Object> validarSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.validarSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/responderSolicitud")
    public ResponseEntity<Object> responderSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.responderSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/aprobarInforme")
    public ResponseEntity<Object> aprobarInforme(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.aprobarInforme(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/rechazarInforme")
    public ResponseEntity<Object> rechazarInforme(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.rechazarInforme(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/aprobarSolicitud")
    public ResponseEntity<Object> aprobarSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.aprobarSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/anularSolicitud")
    public ResponseEntity<Object> anularSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.anularSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/regresarInformeSolicitud")
    public ResponseEntity<Object> regresarInformeSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.regresarSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/regresarNovedadSolicitud")
    public ResponseEntity<Object> regresarNovedadSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.regresarSolicitudForma(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/rechazarSolicitud")
    public ResponseEntity<Object> rechazarSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.rechazarSolicitud(obj);
        return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/tiposAprobacion")
    public ResponseEntity<List<CatalogDTO>> listarTiposAprobacion() {
        final List<CatalogDTO> lista = Arrays.asList(TipoAprobacionSolicitud.values()).parallelStream().map(x -> {
            return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/consulta")
    public ResponseEntity<Page<SolicitudDTO>> consultarFacturas(final Pageable page, @RequestBody final ConsultaSolicitudDTO consulta) {
        final Page<SolicitudDTO> resultadoConsulta = this.servicio.consultar(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping("/estados")
    public ResponseEntity<List<CatalogDTO>> listarEstados() {
        final List<CatalogDTO> lista = Arrays.asList(EstadoSolicitud.values()).parallelStream().map(x -> {
            return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/crearAPartirSolicitudPadre/{idSolicitudPadre}")
    public ResponseEntity<SolicitudEnsayo> crearAPartirSolicitudPadre(@PathVariable("idSolicitudPadre") Long idSolicitudPadre) {
        SolicitudEnsayo respuesta = servicio.crearSolicitudAPartirDeOtra(idSolicitudPadre);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/finalizarProceso")
    public ResponseEntity<Object> finalizarSolicitud(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.finalizarProceso(obj);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/iniciarPruebaEnProceso")
    public ResponseEntity<SolicitudPruebasProceso> iniciarPruebaEnProceso(@RequestBody SolicitudEnsayo obj) {
        SolicitudPruebasProceso respuesta = servicio.iniciarPruebaEnProceso(obj);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/confirmarPlanesAccion")
    public ResponseEntity<Object> confirmarPlanesAccion(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.confirmarPlanesAccion(obj);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/finalizarRevisionPlanesAccion")
    public ResponseEntity<Object> finalizarRevisionPlanesAccion(@RequestBody SolicitudEnsayo obj) {
        boolean respuesta = servicio.finalizarRevisionPlanesAccion(obj);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/pendienteRevisarPlanAccion")
    public ResponseEntity<List<SolicitudEnsayo>> listarPendientesPlanAccion() {
        List<SolicitudEnsayo> respuesta = servicio.obtenerSolicitudesPendientesPlanesAccion();
        return ResponseEntity.ok(respuesta);
    }

}
