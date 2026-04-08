package com.isacore.quality.controller.disenoPavimento;


import com.isacore.quality.model.disenoPavimento.TipoLiganteDto;
import com.isacore.quality.service.disenoPavimento.TipoLiganteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tiposLigante")
public class TipoLiganteController {

    @Autowired
    private TipoLiganteServiceImpl tipoLiganteService;

    @GetMapping()
    public ResponseEntity<List<TipoLiganteDto>> listarTodos() {
        List<TipoLiganteDto> unidades = tipoLiganteService.listar();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("activos")
    public ResponseEntity<List<TipoLiganteDto>> listarActivos() {
        List<TipoLiganteDto> catalogoDefecto = tipoLiganteService.listarActivos();
        return ResponseEntity.ok(catalogoDefecto);
    }

    @PostMapping
    public ResponseEntity<TipoLiganteDto> crear(@RequestBody TipoLiganteDto proveedor) {
        TipoLiganteDto proveedorCreado = tipoLiganteService.registrar(proveedor);
        return ResponseEntity.ok(proveedorCreado);
    }

    @PutMapping
    public ResponseEntity<TipoLiganteDto> modificar(@RequestBody TipoLiganteDto proveedor) {
        TipoLiganteDto obj = tipoLiganteService.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }
}
