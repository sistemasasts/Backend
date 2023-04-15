package com.isacore.quality.controller.cardex;


import com.isacore.quality.model.cardex.InventarioProductoDetalleDto;
import com.isacore.quality.model.cardex.InventarioProductoDto;
import com.isacore.quality.model.cardex.IventarioFiltrosDto;
import com.isacore.quality.model.pnc.ConsultaPncDTO;
import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.model.pnc.PncDTO;
import com.isacore.quality.service.cardex.IInventarioProductoService;
import com.isacore.quality.service.pnc.IDefectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/{id}")
    public ResponseEntity<InventarioProductoDto> listarPorId(@PathVariable("id") long id) {
        InventarioProductoDto inventario = service.listarPorId(id);
        return ResponseEntity.ok(inventario);
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

    @PostMapping("/listarPorCriteriosDetalle")
    public ResponseEntity<Page<InventarioProductoDetalleDto>> listarPorCriterios(final Pageable page, @RequestBody final IventarioFiltrosDto consulta) {
        final Page<InventarioProductoDetalleDto> resultadoConsulta = this.service.listarPorInventarioId(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }
}
