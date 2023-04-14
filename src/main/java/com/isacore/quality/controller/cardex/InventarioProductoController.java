package com.isacore.quality.controller.cardex;


import com.isacore.quality.model.cardex.InventarioProductoDetalleDto;
import com.isacore.quality.model.cardex.InventarioProductoDto;
import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.service.cardex.IInventarioProductoService;
import com.isacore.quality.service.pnc.IDefectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/inventarioProductos")
public class InventarioProductoController {

    @Autowired
    private IInventarioProductoService service;

    @GetMapping
    public ResponseEntity<List<InventarioProductoDto>> listarTodos() {
        List<InventarioProductoDto> unidades = service.listar();
        return ResponseEntity.ok(unidades);
    }

    @PostMapping
    public ResponseEntity<InventarioProductoDto> registrar(@RequestBody InventarioProductoDto proveedor) {
        InventarioProductoDto proveedorCreado = service.registrar(proveedor);
        return ResponseEntity.ok(proveedorCreado);
    }

    @PutMapping
    public ResponseEntity<InventarioProductoDto> modificar(@RequestBody InventarioProductoDto proveedor) {
        InventarioProductoDto obj = service.modificar(proveedor);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/registrarMovimiento")
    public ResponseEntity<InventarioProductoDetalleDto> registrarMovimiento(@RequestBody InventarioProductoDetalleDto proveedor) {
        InventarioProductoDetalleDto detalle = service.registrarMovimiento(proveedor);
        return ResponseEntity.ok(detalle);
    }
}
