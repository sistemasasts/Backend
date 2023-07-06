package com.isacore.quality.controller.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.quality.service.desviacionRequisito.ILoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lote")
public class LoteController {
    @Autowired
    private ILoteService service;

    @GetMapping("/{loteId}")
    public ResponseEntity<Lote> listarPorId(@PathVariable("loteId") Long id) {
        Lote lote = service.listarLotePorId(id);

        return ResponseEntity.ok(lote);
    }

    @GetMapping("/desviacion/{desviacionId}")
    public ResponseEntity<List<Lote>> listarLotesPorDesviacionRequistoId(@PathVariable("desviacionId") Long id) {
        List<Lote> lotes = service.listarLotesPorDesviacionRequisitoId(id);

        return ResponseEntity.ok(lotes);
    }

    @PostMapping
    public ResponseEntity<Lote> crear(@RequestBody Lote lote) {
        Lote nuevoLote = service.create(lote);

        return  ResponseEntity.ok(nuevoLote);
    }

    @PutMapping
    public ResponseEntity<Lote> modificar(@RequestBody Lote lote) {
        Lote loteModificado = service.update(lote);

        return ResponseEntity.ok(loteModificado);
    }

    @DeleteMapping("/{loteId}")
    public ResponseEntity<Boolean> eliminar(@PathVariable("loteId") Long id) {
        boolean response = service.eliminarLotePorId(id);

        return ResponseEntity.ok(response);
    }

}
