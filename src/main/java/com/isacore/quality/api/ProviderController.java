package com.isacore.quality.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.Provider;
import com.isacore.quality.service.IProviderService;

@RestController
@RequestMapping(value = "/providers")
public class ProviderController {

	@Autowired
	private IProviderService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Provider>> listarTodos() {
		List<Provider> proveedores = service.findAll();
		return new ResponseEntity<List<Provider>>(proveedores, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Provider> crear(@RequestBody Provider proveedor) {
		Provider proveedorCreado = service.create(proveedor);
		return new ResponseEntity<Provider>(proveedorCreado, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Provider> modificar(@RequestBody Provider proveedor) {
		Provider obj = service.update(proveedor);
		return new ResponseEntity<Provider>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
		service.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
