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

import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductOrigin;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.service.IProductService;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private IProductService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Product>> listarTodos() {
		List<Product> productos = service.findAllProducts();
		return new ResponseEntity<List<Product>>(productos, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> listarPorId(@PathVariable("id") Integer id) {
		Product product = service.listById(id);
		product.setProperties(null);
		product.setProviders(null);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Product> crear(@RequestBody Product product) {
		Product productCreated = service.create(product);
		return new ResponseEntity<Product>(productCreated, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Product> modificar(@RequestBody Product product) {
		Product obj = service.modify(product);
		return new ResponseEntity<Product>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") String id) {
		service.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/typeProduct")
    public ResponseEntity<List<CatalogDTO>> listTypeProduct() {
		ProductType[] options = ProductType.values();
		List<CatalogDTO> catalog =new ArrayList<>();
		for(ProductType  ae : options ) {
			catalog.add(new CatalogDTO(ae.getDescripcion(), ae.toString()));
		}
        return ResponseEntity.ok(catalog);
    }
	
	@GetMapping("/originProduct")
    public ResponseEntity<List<CatalogDTO>> listOriginProduct() {
		ProductOrigin[] options = ProductOrigin.values();
		List<CatalogDTO> catalog =new ArrayList<>();
		for(ProductOrigin  ae : options ) {
			catalog.add(new CatalogDTO(ae.getDescripcion(), ae.toString()));
		}
        return ResponseEntity.ok(catalog);
    }
	
	@GetMapping(value="/report/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> cotizarDocumento(@PathVariable("id") Integer id) {
		byte [] data = null;
		data= service.generateReport(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/approbationCriteria/{productId}")
	public ResponseEntity<List<InformationAditional>> addCriteria(@PathVariable("productId") Integer productId,
			@RequestBody InformationAditional criteria) {
		List<InformationAditional> criteriaList = service.createInformationAditional(productId, criteria);
		return new ResponseEntity<List<InformationAditional>>(criteriaList, HttpStatus.CREATED);
	}
	
	@GetMapping("/generateNextReview/{id}")
	public ResponseEntity<Object> generateNextReviewById(@PathVariable("id") Integer id) {
		String review = service.updateReview(id);
		return new ResponseEntity<Object>(review, HttpStatus.OK);
	}

}
