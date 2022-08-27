package com.isacore.quality.controller;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.quality.service.spp.ISolicitudPruebaProcesoHistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/solicitudHistorialPP")
public class SolicitudHistorialPruebaProcesoControlador {

	@Autowired
	private ISolicitudPruebaProcesoHistorialService service;
	
	@GetMapping("/{solicitudId}")
	public ResponseEntity<List<SolicitudPruebaProcesoHistorial>> buscarHistorial(@PathVariable("solicitudId") Long solicitudId) throws IOException {
		List<SolicitudPruebaProcesoHistorial> historial = service.buscarHistorial(solicitudId);
		return new ResponseEntity<List<SolicitudPruebaProcesoHistorial>>(historial, HttpStatus.OK);
	}

}
