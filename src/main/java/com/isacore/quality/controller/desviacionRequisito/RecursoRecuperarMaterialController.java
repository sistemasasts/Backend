package com.isacore.quality.controller.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.RecursoRecuperarMaterial;
import com.isacore.quality.service.desviacionRequisito.IRecursoRecuperarMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/recursoMaterial")
public class RecursoRecuperarMaterialController {
    @Autowired
    private IRecursoRecuperarMaterialService service;

    @GetMapping("/desviacion/{desviacionId}")
    public ResponseEntity<List<RecursoRecuperarMaterial>> listarPorDesviacionRequistoId(@PathVariable("desviacionId") Long id) {
        List<RecursoRecuperarMaterial> lotes = service.listarPorDesviacionRequisitoId(id);
        return ResponseEntity.ok(lotes);
    }

    @PostMapping
    public ResponseEntity<RecursoRecuperarMaterial> crear(@RequestBody RecursoRecuperarMaterial lote) {
        RecursoRecuperarMaterial nuevoLote = service.create(lote);
        return ResponseEntity.ok(nuevoLote);
    }

    @PutMapping
    public ResponseEntity<RecursoRecuperarMaterial> modificar(@RequestBody RecursoRecuperarMaterial lote) {
        RecursoRecuperarMaterial loteModificado = service.update(lote);
        return ResponseEntity.ok(loteModificado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable("id") Long id) {
        boolean response = service.eliminarPorId(id);
        return ResponseEntity.ok(response);
    }

}
