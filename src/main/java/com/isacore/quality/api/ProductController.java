package com.isacore.quality.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.isacore.quality.dto.PatronImgenDto;
import com.isacore.quality.dto.ProductoDto;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductOrigin;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.service.IProductService;
import com.isacore.util.CatalogDTO;
import org.springframework.web.multipart.MultipartFile;

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

	@GetMapping("/porNombre/{criterio}")
	public ResponseEntity<List<ProductoDto>> listarPorCriterio(@PathVariable("criterio") String criterio) {
		List<ProductoDto> product = service.listarPorNombreCriterio(criterio);
		return ResponseEntity.ok(product);
	}

	@PostMapping("/imagenPatron/{productoId}")
	public ResponseEntity<Object> subirImagenPatron(@PathVariable("productoId") Integer productoId,
													@RequestPart("file") MultipartFile file) throws IOException {
		service.subirImagenPatron(productoId, file.getBytes(), file.getOriginalFilename(), file.getContentType());
		return ResponseEntity.ok(true);
	}

	@GetMapping("/obtenerImagenPatron/{productoId}")
	public ResponseEntity<PatronImgenDto> obtenerImagenPatron(@PathVariable("productoId") Integer productoId) {
		PatronImgenDto product = service.obtenerImagenPatron(productoId);
		return ResponseEntity.ok(product);
	}

}
