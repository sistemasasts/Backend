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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.dto.ProdProvDTO;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.quality.model.ProviderStatus;
import com.isacore.quality.service.IProdProvService;
import com.isacore.util.CatalogDTO;

@RestController
@RequestMapping(value = "/product_providers")
public class ProdProvController {

	@Autowired
	private IProdProvService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<List<ProdProvDTO>> listarPorId(@PathVariable("id") Integer idProduct) {
		Product productToFound = new Product();
		productToFound.setIdProduct(idProduct);
		List<ProdProvDTO> proveedores = service.findByProduct(productToFound);
		return new ResponseEntity<List<ProdProvDTO>>(proveedores, HttpStatus.OK);
	}
	
	@GetMapping("/proveedores_no_asignados/{id}")
	public ResponseEntity<List<Provider>> listProvidersListAssginNot(@PathVariable("id") Integer idProduct) {
		
		Product product = new Product();
		product.setIdProduct(idProduct);
		List<Provider> proveedores = service.findProviderAssignNot(product);
		return new ResponseEntity<List<Provider>>(proveedores, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> createProvider(@RequestBody ProdProvDTO dto) {
		service.createProdProv(dto);
		return new ResponseEntity<Object>(0, HttpStatus.OK);
	} 
	
	@PutMapping
	public ResponseEntity<Object> updateProviderAssign(@RequestBody ProdProvDTO dto) {
		service.updateProdProv(dto);
		return new ResponseEntity<Object>(0, HttpStatus.OK);
	}
	
	@DeleteMapping("/{idProduct},/{idProvider}")
	public ResponseEntity<Object> eliminar(@PathVariable("idProduct") Integer idProduct, @PathVariable("idProvider") Integer idProvider) {
		service.deleteByProductAndProvider(idProduct, idProvider);
		return new ResponseEntity<Object>(0,HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/status")
    public ResponseEntity<List<CatalogDTO>> listStatusProvider() {
		ProviderStatus[] options = ProviderStatus.values();
		List<CatalogDTO> catalog =new ArrayList<>();
		for(ProviderStatus  ae : options ) {
			catalog.add(new CatalogDTO(ae.getDescripcion(), ae.toString()));
		}
        return ResponseEntity.ok(catalog);
    }
	
}
