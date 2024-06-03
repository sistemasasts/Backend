package com.isacore.quality.api;

import com.isacore.quality.dto.ProveedorDto;
import com.isacore.quality.model.Provider;
import com.isacore.quality.service.IProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/productId/{productId}")
    public ResponseEntity<Object> listarPorProducto(@PathVariable("productId") Integer productId) {
        List<Provider> proveedores = service.findByProductId(productId);
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/porNombre/{criterio}")
    public ResponseEntity<List<ProveedorDto>> listarPorCriterio(@PathVariable("criterio") String criterio) {
        List<ProveedorDto> product = service.listarPorNombreCriterio(criterio);
        return ResponseEntity.ok(product);
    }

}
