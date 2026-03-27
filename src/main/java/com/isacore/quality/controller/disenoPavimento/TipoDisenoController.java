package com.isacore.quality.controller.disenoPavimento;


import com.isacore.quality.model.disenoPavimento.TipoDisenoDto;
import com.isacore.quality.service.disenoPavimento.TipoDisenoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tiposDiseno")
public class TipoDisenoController {

    @Autowired
    private TipoDisenoServiceImpl tipoDisenoService;

    @GetMapping()
    public ResponseEntity<List<TipoDisenoDto>> listarTodos() {
        List<TipoDisenoDto> unidades = tipoDisenoService.listar();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("activos")
    public ResponseEntity<List<TipoDisenoDto>> listarActivos() {
        List<TipoDisenoDto> catalogoDefecto = tipoDisenoService.listarActivos();
        return ResponseEntity.ok(catalogoDefecto);
    }

    @PostMapping
    public ResponseEntity<TipoDisenoDto> crear(@RequestBody TipoDisenoDto proveedor) {
        TipoDisenoDto proveedorCreado = tipoDisenoService.registrar(proveedor);
        return ResponseEntity.ok(proveedorCreado);
    }

    @PutMapping
    public ResponseEntity<TipoDisenoDto> modificar(@RequestBody TipoDisenoDto proveedor) {
        TipoDisenoDto obj = tipoDisenoService.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }
}
