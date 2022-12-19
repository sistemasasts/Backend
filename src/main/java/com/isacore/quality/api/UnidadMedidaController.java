package com.isacore.quality.api;

import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.service.IProviderService;
import com.isacore.quality.service.IUnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/unidadesMedida")
public class UnidadMedidaController {

	@Autowired
	private IUnidadMedidaService service;

	@GetMapping()
	public ResponseEntity<List<UnidadMedida>> listarTodos() {
		List<UnidadMedida> unidades = service.findAll();
		return ResponseEntity.ok(unidades);
	}

    @GetMapping("activos")
    public ResponseEntity<List<UnidadMedida>> listarActivos() {
        List<UnidadMedida> unidades = service.listarActivos();
        return ResponseEntity.ok(unidades);
    }

	@PostMapping
	public ResponseEntity<UnidadMedida> crear(@RequestBody UnidadMedida proveedor) {
		UnidadMedida proveedorCreado = service.create(proveedor);
		return ResponseEntity.ok(proveedorCreado);
	}

	@PutMapping
	public ResponseEntity<UnidadMedida> modificar(@RequestBody UnidadMedida proveedor) {
		UnidadMedida obj = service.update(proveedor);
		return ResponseEntity.ok(obj);
	}
}
