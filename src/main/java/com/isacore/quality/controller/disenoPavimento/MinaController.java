package com.isacore.quality.controller.disenoPavimento;


import com.isacore.quality.model.disenoPavimento.MinaDto;
import com.isacore.quality.service.disenoPavimento.MinaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/minas")
public class MinaController {

    @Autowired
    private MinaServiceImpl minaService;

    @GetMapping("/{id}")
    public ResponseEntity<MinaDto> listarPorId(@PathVariable("id") long id) {
        MinaDto minaDtos = minaService.obtenerPorId(id);
        return ResponseEntity.ok(minaDtos);
    }

    @GetMapping()
    public ResponseEntity<List<MinaDto>> listarTodos() {
        List<MinaDto> minas = minaService.listar();
        return ResponseEntity.ok(minas);
    }

    @GetMapping("activos")
    public ResponseEntity<List<MinaDto>> listarActivos() {
        List<MinaDto> minaDtos = minaService.listarActivos();
        return ResponseEntity.ok(minaDtos);
    }

    @PostMapping
    public ResponseEntity<MinaDto> crear(@RequestBody MinaDto dto) {
        MinaDto mina = minaService.registrar(dto);
        return ResponseEntity.ok(mina);
    }

    @PutMapping
    public ResponseEntity<MinaDto> modificar(@RequestBody MinaDto dto) {
        MinaDto obj = minaService.actualizar(dto);
        return ResponseEntity.ok(obj);
    }
}
