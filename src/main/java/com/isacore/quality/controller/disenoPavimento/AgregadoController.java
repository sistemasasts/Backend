package com.isacore.quality.controller.disenoPavimento;


import com.isacore.quality.model.disenoPavimento.AgregadoDto;
import com.isacore.quality.service.disenoPavimento.AgregadoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/agregados")
public class AgregadoController {

    @Autowired
    private AgregadoServiceImpl agregadoService;

    @GetMapping()
    public ResponseEntity<List<AgregadoDto>> listarTodos() {
        List<AgregadoDto> agregados = agregadoService.listar();
        return ResponseEntity.ok(agregados);
    }

    @GetMapping("activos")
    public ResponseEntity<List<AgregadoDto>> listarActivos() {
        List<AgregadoDto> agregadoDtoList = agregadoService.listarActivos();
        return ResponseEntity.ok(agregadoDtoList);
    }

    @PostMapping
    public ResponseEntity<AgregadoDto> crear(@RequestBody AgregadoDto proveedor) {
        AgregadoDto agregado = agregadoService.registrar(proveedor);
        return ResponseEntity.ok(agregado);
    }

    @PutMapping
    public ResponseEntity<AgregadoDto> modificar(@RequestBody AgregadoDto proveedor) {
        AgregadoDto obj = agregadoService.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }
}
