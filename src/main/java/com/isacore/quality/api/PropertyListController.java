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

import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.service.IPropertyListService;

@RestController
@RequestMapping(value = "/propertylists")
public class PropertyListController {

	@Autowired
	private IPropertyListService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PropertyList>> listarTodos() {
		List<PropertyList> propiedades = service.findAll();
		
		return new ResponseEntity<List<PropertyList>>(propiedades, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PropertyList> crear(@RequestBody PropertyList propiedad) {
		PropertyList propiedadCreada = service.create(propiedad);
		return new ResponseEntity<PropertyList>(propiedadCreada, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<PropertyList> modificar(@RequestBody PropertyList propiedad) {
		PropertyList obj = service.update(propiedad);
		return new ResponseEntity<PropertyList>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
		service.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value="/normsAssignNot/{idProp}" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LaboratoryNorm>> listarNormasNoAsigandas(@PathVariable("idProp") String idProp) {
		List<LaboratoryNorm> normas = service.findNormsAssignNot(idProp);		
		return new ResponseEntity<List<LaboratoryNorm>>(normas, HttpStatus.OK);
	}
}
