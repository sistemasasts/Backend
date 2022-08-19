package com.isacore.quality.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.ConfigurationFormTestEntryMP;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.service.IConfigurationFormTestEntryMPService;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/configurationsFormEntry")
public class ConfigurationFormTestEntryMPController {

	@Autowired
	private IConfigurationFormTestEntryMPService service;
	
	@GetMapping("/{poductTypeText}")
	public ResponseEntity<List<ConfigurationFormTestEntryMP>> listConfigurations(@PathVariable("poductTypeText") String productTypeText) {
		List<ConfigurationFormTestEntryMP> configuration = service.listByProductTypeText(productTypeText);
		return new ResponseEntity<List<ConfigurationFormTestEntryMP>>(configuration, HttpStatus.OK);
	}
	
	@GetMapping("/onlyProperties/{poductTypeText}")
	public ResponseEntity<List<PropertyList>> listOnlyPropertiesConfigurations(@PathVariable("poductTypeText") String productTypeText) {
		List<PropertyList> configuration = service.listOnlyPropertiesByProductTypeText(productTypeText);
		return new ResponseEntity<List<PropertyList>>(configuration, HttpStatus.OK);
	}
	
	@GetMapping("/catalog")
	public ResponseEntity<List<CatalogDTO>> catalogProductTypeText() {
		List<CatalogDTO> catalog = new ArrayList<>();
		List<String> configuration = service.listCatalogProductTypeText();
		configuration.forEach(x -> catalog.add(new CatalogDTO(x, x)));
		
		return new ResponseEntity<List<CatalogDTO>>(catalog, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ConfigurationFormTestEntryMP> crear(@RequestBody ConfigurationFormTestEntryMP configFormTest) {
		ConfigurationFormTestEntryMP configCreado = service.create(configFormTest);
		return new ResponseEntity<ConfigurationFormTestEntryMP>(configCreado, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
		service.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
