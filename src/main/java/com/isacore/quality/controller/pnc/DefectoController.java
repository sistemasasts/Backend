package com.isacore.quality.controller.pnc;


import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.service.pnc.IDefectoService;
import com.isacore.util.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/defectos")
public class DefectoController {

    @Autowired
    private IDefectoService service;

    @GetMapping()
    public ResponseEntity<List<Defecto>> listarTodos() {
        List<Defecto> unidades = service.findAll();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("activos")
    public ResponseEntity<List<Defecto>> listarActivos() {
        List<Defecto> catalogoDefecto = service.listarActivos();
//                .stream()
//                .map(x -> new CatalogDTO(x.getNombre(), String.valueOf(x.getId())))
//                .collect(Collectors.toList());
        return ResponseEntity.ok(catalogoDefecto);
    }

    @PostMapping
    public ResponseEntity<Defecto> crear(@RequestBody Defecto proveedor) {
        Defecto proveedorCreado = service.create(proveedor);
        return ResponseEntity.ok(proveedorCreado);
    }

    @PutMapping
    public ResponseEntity<Defecto> modificar(@RequestBody Defecto proveedor) {
        Defecto obj = service.update(proveedor);
        return ResponseEntity.ok(obj);
    }
}
