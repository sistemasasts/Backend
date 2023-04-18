package com.isacore.quality.controller.bitacora;

import com.isacore.quality.model.bitacora.Bitacora;
import com.isacore.quality.service.bitacora.IBitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/bitacora")
public class BitacoraController {

    @Autowired
    private IBitacoraService service;

    @GetMapping()
    public ResponseEntity<List<Bitacora>> listarTodos() {
        List<Bitacora> listadoBitacora = service.findAll();

        return ResponseEntity.ok(listadoBitacora);
    }

    @GetMapping("activos")
    public ResponseEntity<List<Bitacora>> listarBitacorasActivas() {
        List<Bitacora> bitacorasActivas = service.listarBitacorasActivas();

        return ResponseEntity.ok(bitacorasActivas);
    }

    @PostMapping
    public ResponseEntity<Bitacora> crear(@RequestBody Bitacora bitacora) {
        Bitacora nuevaBitacora = service.create(bitacora);

        return ResponseEntity.ok(nuevaBitacora);
    }

    @PutMapping
    public ResponseEntity<Bitacora> modificar(@RequestBody Bitacora bitacora) {
        Bitacora bitacoraModificada = service.update(bitacora);

        return ResponseEntity.ok(bitacoraModificada);
    }
}
