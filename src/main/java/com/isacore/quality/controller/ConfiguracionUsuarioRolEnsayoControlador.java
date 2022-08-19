package com.isacore.quality.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.service.se.IConfiguracionUsuarioRolEnsayo;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/configuracionesse")
public class ConfiguracionUsuarioRolEnsayoControlador {

	@Autowired
	private IConfiguracionUsuarioRolEnsayo servicio;
	
	@GetMapping
	public ResponseEntity<List<ConfiguracionUsuarioRolEnsayo>> listarConfiguraciones() {
		List<ConfiguracionUsuarioRolEnsayo> preCierreCaja = servicio.findAll();
		return ResponseEntity.ok(preCierreCaja);
	}
	
	@GetMapping("/tipoSolicitud")
    public ResponseEntity<List<CatalogDTO>> listarTiposSolicitud() {
        final List<CatalogDTO> lista = Arrays.asList(TipoSolicitud.SOLICITUD_ENSAYOS, TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO).parallelStream().map( x -> {
        	return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(lista);
    }
	
	@GetMapping("/ordenFlujo")
    public ResponseEntity<List<CatalogDTO>> listarOrdenFlujo() {
        final List<CatalogDTO> lista = Arrays.asList(OrdenFlujo.VALIDAR_SOLICITUD, OrdenFlujo.RESPONDER_SOLICITUD, OrdenFlujo.APROBAR_INFORME).parallelStream().map( x -> {
        	return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
	
	@PostMapping
	public ResponseEntity<ConfiguracionUsuarioRolEnsayo> crear(@RequestBody ConfiguracionUsuarioRolEnsayo obj) {
		ConfiguracionUsuarioRolEnsayo normCreated = servicio.create(obj);
		return new ResponseEntity<ConfiguracionUsuarioRolEnsayo>(normCreated, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ConfiguracionUsuarioRolEnsayo> modificar(@RequestBody ConfiguracionUsuarioRolEnsayo norm) {
		ConfiguracionUsuarioRolEnsayo obj = servicio.update(norm);
		return new ResponseEntity<ConfiguracionUsuarioRolEnsayo>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
        servicio.delete(id.toString());
        return new ResponseEntity<Object>(HttpStatus.OK);
    } 
	
}
