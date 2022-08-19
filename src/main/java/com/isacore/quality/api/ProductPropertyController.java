package com.isacore.quality.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.dto.ProductPropertyDTO;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.service.IPropertyService;

@RestController
@RequestMapping(value = "/product_properties")
public class ProductPropertyController {

	@Autowired
	private IPropertyService service;
	
	@GetMapping("/especificaciones/{id}")
	public ResponseEntity<List<ProductPropertyDTO>> listarPorId(@PathVariable("id") Integer idProduct) {
		Product productToFound = new Product();
		productToFound.setIdProduct(idProduct);
		List<ProductPropertyDTO> propiedades = service.findByProduct(productToFound);
		return new ResponseEntity<List<ProductPropertyDTO>>(propiedades, HttpStatus.OK);
	}
	
	@GetMapping("/propiedades_no_asignadas/{id}")
	public ResponseEntity<List<PropertyList>> listPropertyListAssginNot(@PathVariable("id") Integer idProduct) {
		
		Product product = new Product();
		product.setIdProduct(idProduct);
		List<PropertyList> propiedades = service.findPropertyAssignNot(product);
		return new ResponseEntity<List<PropertyList>>(propiedades, HttpStatus.OK);
	}
	
	/*TODO Método que sirve para realizar inserciones, modificaciones de las especificaciones de cada producto*/
	@PutMapping
	public ResponseEntity<Object> modificarPropiedades(@RequestBody List<ProductPropertyDTO> especificaciones) {
		service.updatePropertiesByProduct(especificaciones);
		return new ResponseEntity<Object>(0, HttpStatus.OK);
	}
	
	@DeleteMapping("/{idProduct},/{idPropertyList}")
	public ResponseEntity<Object> eliminar(@PathVariable("idProduct") Integer idProduct, @PathVariable("idPropertyList") String idPropertyList) {
		service.deleteByProductAndProperty(idProduct, idPropertyList);
		return new ResponseEntity<Object>(0,HttpStatus.NO_CONTENT);
	}
	
}
