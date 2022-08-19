package com.isacore.quality.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.service.IApprobationCriteriaFileService;

@RestController
@RequestMapping(value = "/informationAditionalFile")
public class InformationAditionalFileController {

	@Autowired
	private IApprobationCriteriaFileService service;
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		service.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
