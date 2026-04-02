package com.isacore.quality.controller.disenoPavimento;


import com.isacore.quality.model.disenoPavimento.AgregadoQuimicaDto;
import com.isacore.quality.service.disenoPavimento.AgregadoQuimicaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/agregadosQuimica")
public class AgregadoQuimicaController {

    @Autowired
    private AgregadoQuimicaServiceImpl agregadoService;

    @GetMapping()
    public ResponseEntity<List<AgregadoQuimicaDto>> listarTodos() {
        List<AgregadoQuimicaDto> agregados = agregadoService.listar();
        return ResponseEntity.ok(agregados);
    }

    @GetMapping("activos")
    public ResponseEntity<List<AgregadoQuimicaDto>> listarActivos() {
        List<AgregadoQuimicaDto> agregadoDtoList = agregadoService.listarActivos();
        return ResponseEntity.ok(agregadoDtoList);
    }

    @PostMapping
    public ResponseEntity<AgregadoQuimicaDto> crear(@RequestBody AgregadoQuimicaDto proveedor) {
        AgregadoQuimicaDto agregado = agregadoService.registrar(proveedor);
        return ResponseEntity.ok(agregado);
    }

    @PutMapping
    public ResponseEntity<AgregadoQuimicaDto> modificar(@RequestBody AgregadoQuimicaDto proveedor) {
        AgregadoQuimicaDto obj = agregadoService.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }
}
