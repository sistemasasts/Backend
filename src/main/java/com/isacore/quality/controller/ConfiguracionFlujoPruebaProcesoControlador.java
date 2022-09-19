package com.isacore.quality.controller;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.ConfiguracionFlujoPruebaProceso;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.service.spp.IConfiguracionFlujoPruebaProceso;
import com.isacore.util.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/configuracionesFlujoPP")
public class ConfiguracionFlujoPruebaProcesoControlador {

	@Autowired
	private IConfiguracionFlujoPruebaProceso servicio;
	
	@GetMapping
	public ResponseEntity<List<ConfiguracionFlujoPruebaProceso>> listarConfiguraciones() {
		List<ConfiguracionFlujoPruebaProceso> preCierreCaja = servicio.findAll();
		return ResponseEntity.ok(preCierreCaja);
	}
	
	@GetMapping("/tipoSolicitud")
    public ResponseEntity<List<CatalogDTO>> listarTiposSolicitud() {
        final List<CatalogDTO> lista = Arrays.asList(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO).stream().map( x -> {
        	return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(lista);
    }
	
	@GetMapping("/ordenFlujo")
    public ResponseEntity<List<CatalogDTO>> listarOrdenFlujo() {
        final List<CatalogDTO> lista = Arrays.asList(OrdenFlujoPP.VALIDAR_SOLICITUD, OrdenFlujoPP.APROBAR_SOLICITUD, OrdenFlujoPP.ASIGNAR_RESPONSABLE, OrdenFlujoPP.CALIDAD,
				OrdenFlujoPP.MANTENIMIENTO, OrdenFlujoPP.PRODUCCION).parallelStream().map(x -> {
        	return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
	
	@PostMapping
	public ResponseEntity<ConfiguracionFlujoPruebaProceso> crear(@RequestBody ConfiguracionFlujoPruebaProceso obj) {
		ConfiguracionFlujoPruebaProceso normCreated = servicio.create(obj);
		return new ResponseEntity<ConfiguracionFlujoPruebaProceso>(normCreated, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ConfiguracionFlujoPruebaProceso> modificar(@RequestBody ConfiguracionFlujoPruebaProceso norm) {
		ConfiguracionFlujoPruebaProceso obj = servicio.update(norm);
		return new ResponseEntity<ConfiguracionFlujoPruebaProceso>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
        servicio.delete(id.toString());
        return new ResponseEntity<Object>(HttpStatus.OK);
    } 
	
}
