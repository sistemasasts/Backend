package com.isacore.quality.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.quality.service.se.ISolicitudDocumentoService;

@RestController
@RequestMapping(value = "/solicitudDocumentos")
public class SolicitudDocumentoControlador {

	@Autowired
	private ISolicitudDocumentoService service;
	
	@PostMapping
	public ResponseEntity<SolicitudDocumento> subirArchivo(@RequestPart("info") String info, @RequestPart("file") MultipartFile file) throws IOException {
		SolicitudDocumento infoRegister = service.subir(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
		return new ResponseEntity<SolicitudDocumento>(infoRegister, HttpStatus.OK);
	}
	
	@PostMapping("/solicitudPruebasProceso")
	public ResponseEntity<SolicitudDocumento> subirArchivoPruebasProceso(@RequestPart("info") String info, @RequestPart("file") MultipartFile file) throws IOException {
		SolicitudDocumento infoRegister = service.subirParaSolicitudPruebasProceso(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
		return new ResponseEntity<SolicitudDocumento>(infoRegister, HttpStatus.OK);
	}
	
	@GetMapping("/{estado}/{orden}/{solicitudId}")
	public ResponseEntity<List<SolicitudDocumento>> listarArchivos(@PathVariable("estado") EstadoSolicitud estado, @PathVariable("orden") OrdenFlujo orden, @PathVariable("solicitudId") Long solicitudId) throws IOException {
		List<SolicitudDocumento> files = service.buscarPorEstadoYOrdenYSolicitudId(estado, orden, solicitudId);
		return new ResponseEntity<List<SolicitudDocumento>>(files, HttpStatus.OK);
	}
	
	@GetMapping("/solicitudPruebasProceso/{estado}/{orden}/{solicitudId}")
	public ResponseEntity<List<SolicitudDocumento>> listarArchivosPruebasProceso(@PathVariable("estado") EstadoSolicitud estado, @PathVariable("orden") OrdenFlujo orden, @PathVariable("solicitudId") Long solicitudId) throws IOException {
		List<SolicitudDocumento> files = service.buscarPorEstadoYOrdenYSolicitudPruebaProcesoId(estado, orden, solicitudId);
		return new ResponseEntity<List<SolicitudDocumento>>(files, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		boolean resultado = service.eliminar(id);
		return new ResponseEntity<Object>(resultado,HttpStatus.OK);
	}
	
	@GetMapping(value="/ver/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> ver(@PathVariable("id") Long id) {
		byte [] data = null;
		data= service.descargar(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@GetMapping(value="/comprimido/{historialId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> descargarComprimido(@PathVariable("historialId") Long id) {
		byte [] data = null;
		data= service.descargarPorHistorialId(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
}
