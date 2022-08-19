package com.isacore.quality.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.se.SolicitudPruebasProceso;
import com.isacore.quality.service.se.ISolicitudPruebasProcesoService;

@RestController
@RequestMapping(value = "/solicitudesPruebasProceso")
public class SolicitudPruebasProcesoControlador {

	@Autowired
	private ISolicitudPruebasProcesoService servicio;
	
	@GetMapping("/nombreSolicitante")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudesPorNombreSolicitante() {
		List<SolicitudPruebasProceso> solicitudes = servicio.obtenerSolicitudesPorUsuarioSolicitante();
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/usuarioGestion")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudesPorUsuarioGestion() {
		List<SolicitudPruebasProceso> solicitudes = servicio.obtenerSolicitudesPorUsuarioEnGestion();
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/usuarioValidador")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudesPorUsuarioValidador() {
		List<SolicitudPruebasProceso> solicitudes = servicio.obtenerSolicitudesPorUsuarioValidador();
		return ResponseEntity.ok(solicitudes);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SolicitudPruebasProceso> buscarSolicitud(@PathVariable("id") Long id) {
		SolicitudPruebasProceso solicitudes = servicio.buscarPorId(id);
		return ResponseEntity.ok(solicitudes);
	}
	
	@PostMapping
	public ResponseEntity<SolicitudPruebasProceso> crear(@RequestBody SolicitudPruebasProceso obj) {
		SolicitudPruebasProceso solicitud = servicio.create(obj);
		return new ResponseEntity<SolicitudPruebasProceso>(solicitud, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<SolicitudPruebasProceso> actualizar(@RequestBody SolicitudPruebasProceso obj) {
		SolicitudPruebasProceso solicitud = servicio.update(obj);
		return new ResponseEntity<SolicitudPruebasProceso>(solicitud, HttpStatus.OK);
	}
	
	
	@PostMapping("/enviarSolicitud")
	public ResponseEntity<Object> enviarSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.enviarSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/validarSolicitud")
	public ResponseEntity<Object> validarSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.validarSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/responderSolicitud")
	public ResponseEntity<Object> responderSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.responderSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/anularSolicitud")
	public ResponseEntity<Object> anularSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.anularSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/regresarInformeSolicitud")
	public ResponseEntity<Object> regresarInformeSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.regresarSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@PostMapping("/rechazarSolicitud")
	public ResponseEntity<Object> rechazarSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.rechazarSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
}
