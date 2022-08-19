package com.isacore.quality.api;

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

import com.isacore.quality.dto.OutputMaterialDTO;
import com.isacore.quality.model.ExitMaterialHistory;
import com.isacore.quality.service.IExitMaterialHistoryService;

@RestController
@RequestMapping(value = "/exitmaterials")
public class ExitMaterialHistoryController {

	@Autowired
	private IExitMaterialHistoryService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<OutputMaterialDTO> listarPorIdPNC(@PathVariable("id") Integer id) {
		OutputMaterialDTO salidaMaterial = service.buscarPorIdPNC(id);
		return new ResponseEntity<OutputMaterialDTO>(salidaMaterial, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ExitMaterialHistory> crear(@RequestBody ExitMaterialHistory salidaMaerial) {
		ExitMaterialHistory salidaMateriaCreado = service.create(salidaMaerial);
		return new ResponseEntity<ExitMaterialHistory>(salidaMateriaCreado, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ExitMaterialHistory> modificar(@RequestBody ExitMaterialHistory proveedor) {
		ExitMaterialHistory obj = service.update(proveedor);
		return new ResponseEntity<ExitMaterialHistory>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
		service.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
