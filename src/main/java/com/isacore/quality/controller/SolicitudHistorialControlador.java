package com.isacore.quality.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.service.se.ISolicitudHistorialService;

@RestController
@RequestMapping(value = "/solicitudHistorial")
public class SolicitudHistorialControlador {

	@Autowired
	private ISolicitudHistorialService service;
	
	@GetMapping("/{solicitudId}")
	public ResponseEntity<List<SolicitudHistorial>> buscarHistorial(@PathVariable("solicitudId") Long solicitudId) throws IOException {
		List<SolicitudHistorial> historial = service.buscarHistorial(solicitudId);
		return new ResponseEntity<List<SolicitudHistorial>>(historial, HttpStatus.OK);
	}
	
	@GetMapping("pruebasProceso/{solicitudId}")
	public ResponseEntity<List<SolicitudHistorial>> buscarHistorialPruebasProceso(@PathVariable("solicitudId") Long solicitudId) throws IOException {
		List<SolicitudHistorial> historial = service.buscarHistorialPruebasProceso(solicitudId);
		return new ResponseEntity<List<SolicitudHistorial>>(historial, HttpStatus.OK);
	}
}
