package com.isacore.quality.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.isacore.quality.model.Area;
import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.spp.*;
import com.isacore.quality.service.IAreasService;
import com.isacore.util.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.service.se.ISolicitudPruebasProcesoService;

@RestController
@RequestMapping(value = "/solicitudesPruebasProceso")
public class SolicitudPruebasProcesoControlador {

	@Autowired
	private ISolicitudPruebasProcesoService servicio;

	@Autowired
	private IAreasService servicioArea;

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

	@PostMapping("/asignarResponsable")
	public ResponseEntity<Object> asignarResponsableSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.asignarResponsable(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}

	@PostMapping("/reasignarResponsable")
	public ResponseEntity<Object> reasignarResponsableSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.reasignarResponsable(obj);
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/solicitudesAsignadas/{orden}")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudAsignadas(@PathVariable("orden") OrdenFlujoPP orden) {
		List<SolicitudPruebasProceso> respuesta = servicio.obtenerSolicitudesPorProcesar(orden);
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/solicitudesPorAsignar/{orden}")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudPorAsignar(@PathVariable("orden") OrdenFlujoPP orden) {
		List<SolicitudPruebasProceso> respuesta = servicio.obtenerSolicitudesPorAsignarResponsable(orden);
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/solicitudesPorReasignar/{orden}")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudPorReasignar(@PathVariable("orden") OrdenFlujoPP orden) {
		List<SolicitudPruebasProceso> respuesta = servicio.obtenerSolicitudesPorReasignarResponsable(orden);
		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/marcarPruebaNoRealizada")
	public ResponseEntity<Object> marcarPruebaNoRealizada(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.marcarComoPruebaNoRealizada(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}

	@PostMapping("/marcarPruebaNoRealizadaDefinitiva")
	public ResponseEntity<Object> marcarPruebaNoRealizadaDefinitiva(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.marcarComoPruebaNoRealizadaDefinitiva(obj);
		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/procesar")
	public ResponseEntity<Object> responderSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		servicio.procesar(obj);
		return ResponseEntity.ok(true);
	}

	@PostMapping("/procesarAprobacion")
	public ResponseEntity<Object> procesarAprobacion(@RequestBody AprobarSolicitudDTO obj) {
		servicio.procesarAprobacion(obj);
		return ResponseEntity.ok(true);
	}

	@GetMapping("/solicitudesPorAprobar/{orden}")
	public ResponseEntity<List<SolicitudPruebasProceso>> listarSolicitudPorAprobar(@PathVariable("orden") OrdenFlujoPP orden) {
		List<SolicitudPruebasProceso> respuesta = servicio.obtenerSolicitudesPorAprobar(orden);
		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/anularSolicitud")
	public ResponseEntity<Object> anularSolicitud(@RequestBody SolicitudPruebasProceso obj) {
		boolean respuesta = servicio.anularSolicitud(obj);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}

    @PostMapping("/rechazarSolicitud")
    public ResponseEntity<Object> rechazarSolicitud(@RequestBody SolicitudPruebasProceso obj) {
        boolean respuesta = servicio.rechazarSolicitud(obj);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/regresarSolicitudNovedadForma")
    public ResponseEntity<Object> regresarSolicitudNovedadForma(@RequestBody SolicitudPruebasProceso obj) {
        boolean respuesta = servicio.regresarSolicitudNovedadForma(obj);
        return ResponseEntity.ok(respuesta);
    }

	@GetMapping("/areas")
	public ResponseEntity<List<Area>> listarAreas() {
		List<Area> areas = servicioArea.findAll();
		return ResponseEntity.ok(areas);
	}

	@GetMapping("/origenes")
	public ResponseEntity<List<CatalogDTO>> listarOrigenes() {
		List<CatalogDTO> catalgo = new ArrayList<>();
		for(OrigenSolicitudPP origen : OrigenSolicitudPP.values()){
			catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
		}
		return ResponseEntity.ok(catalgo);
	}

	@GetMapping("/tiposAprobacion")
	public ResponseEntity<List<CatalogDTO>> listarTiposAprobacion() {
		List<CatalogDTO> catalgo = new ArrayList<>();
		for(TipoAprobacionPP origen : TipoAprobacionPP.values()){
			catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString(), origen.isAprobado()));
		}
		return ResponseEntity.ok(catalgo);
	}

	@GetMapping(value="/reporte/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporte(@PathVariable("id") long id) {
		byte [] data = null;
		data= servicio.generateReporte(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}

	@GetMapping("/repetirPrueba/{solicitudId}")
	public ResponseEntity<SolicitudPruebasProceso> repetirPrueba(@PathVariable("solicitudId") long solicitudId) {
		SolicitudPruebasProceso solicitud= servicio.crearSolicitudParaRepetirPrueba(solicitudId);
		return new ResponseEntity<SolicitudPruebasProceso>(solicitud, HttpStatus.OK);
	}

	@PostMapping("/consulta")
	public ResponseEntity<Page<SolicitudPPDTO>> consultar(final Pageable page, @RequestBody final ConsultaSolicitudPPDTO consulta) {
		final Page<SolicitudPPDTO> resultadoConsulta = this.servicio.consultar(page, consulta);
		return ResponseEntity.ok(resultadoConsulta);
	}

	@GetMapping("/estados")
	public ResponseEntity<List<CatalogDTO>> listarEstados() {
		final List<CatalogDTO> lista = Arrays.asList(EstadoSolicitudPP.values()).parallelStream().map(x -> {
			return new CatalogDTO(x.getDescripcion(), x.toString());
		}).collect(Collectors.toList());
		return ResponseEntity.ok(lista);
	}

	@PostMapping("/agregarMaterialFormula/{solicitudId}")
	public ResponseEntity<SolicitudPruebasProceso> agregarMaterialFormula(@PathVariable("solicitudId") long solicitudId, @RequestBody MaterialFormula materialFormula) {
		SolicitudPruebasProceso resultadoConsulta = this.servicio.agregarMaterialFormula(solicitudId, materialFormula);
		return ResponseEntity.ok(resultadoConsulta);
	}

	@PostMapping("/editarMaterialFormula/{solicitudId}")
	public ResponseEntity<SolicitudPruebasProceso> editarMaterialFormula(@PathVariable("solicitudId") long solicitudId, @RequestBody MaterialFormula materialFormula) {
		SolicitudPruebasProceso resultadoConsulta = this.servicio.editarMaterialFormula(solicitudId, materialFormula);
		return ResponseEntity.ok(resultadoConsulta);
	}

	@GetMapping("/eliminarMaterialFormula/{solicitudId}/{materialFormulaId}")
	public ResponseEntity<SolicitudPruebasProceso> eliminarMaterialFormula(@PathVariable("solicitudId") long solicitudId, @PathVariable("materialFormulaId") long materialFormulaId) {
		SolicitudPruebasProceso resultadoConsulta = this.servicio.eliminarMaterialFormula(solicitudId, materialFormulaId);
		return ResponseEntity.ok(resultadoConsulta);
	}

}
