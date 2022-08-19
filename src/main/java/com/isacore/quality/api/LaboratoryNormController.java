package com.isacore.quality.api;

import java.util.ArrayList;
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
import com.isacore.quality.model.NormState;
import com.isacore.quality.service.ILaboratoryNormService;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/laboratoryNorms")
public class LaboratoryNormController {

	@Autowired
	private ILaboratoryNormService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LaboratoryNorm>> listarTodos() {
		List<LaboratoryNorm> norms = service.findAll();
		return new ResponseEntity<List<LaboratoryNorm>>(norms, HttpStatus.OK);
	}
	
	@GetMapping(value="/vigentes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LaboratoryNorm>> listarVigentes() {
		List<LaboratoryNorm> norms = service.listNormsVigentes();
		return new ResponseEntity<List<LaboratoryNorm>>(norms, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<LaboratoryNorm> crear(@RequestBody LaboratoryNorm obj) {
		LaboratoryNorm normCreated = service.create(obj);
		return new ResponseEntity<LaboratoryNorm>(normCreated, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<LaboratoryNorm> modificar(@RequestBody LaboratoryNorm norm) {
		LaboratoryNorm obj = service.update(norm);
		return new ResponseEntity<LaboratoryNorm>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		service.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("/listState")
    public ResponseEntity<List<CatalogDTO>> listNormState() {
		NormState[] options = NormState.values();
		List<CatalogDTO> catalog =new ArrayList<>();
		for(NormState  ae : options ) {
			catalog.add(new CatalogDTO(ae.toString(), ae.toString()));
		}
        return ResponseEntity.ok(catalog);
    }
}
