package com.isacore.quality.controller.pnc;

import com.isacore.quality.model.pnc.*;
import com.isacore.quality.service.pnc.IPncHistorialService;
import com.isacore.quality.service.pnc.IPncSalidaMaterialService;
import com.isacore.util.CatalogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/pncSalidasMaterial")
public class PncSalidaMaterialController {

    private final IPncSalidaMaterialService service;
    private final IPncHistorialService historialService;

    @PostMapping
    public ResponseEntity<PncSalidaMaterialDto> crear(@RequestBody PncSalidaMaterialDto dto) {
        PncSalidaMaterialDto proveedorCreado = service.registrar(dto);
        return ResponseEntity.ok(proveedorCreado);
    }

    @GetMapping("/pnc/{pncId}")
    public ResponseEntity<List<PncSalidaMaterialDto>> listarTodosPorIdPnc(@PathVariable("pncId") long pncId) {
        List<PncSalidaMaterialDto> obj = service.listarPorPncId(pncId);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PncSalidaMaterialDto> listarPorId(@PathVariable("id") long id) {
        PncSalidaMaterialDto obj = service.listarPorId(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/completo/{id}")
    public ResponseEntity<PncSalidaMaterial> listarPorIdCompleto(@PathVariable("id") long id) {
        PncSalidaMaterial obj = service.listarPorIdCompleto(id);
        return ResponseEntity.ok(obj);
    }

    @PutMapping
    public ResponseEntity<PncSalidaMaterialDto> modificar(@RequestBody PncSalidaMaterialDto proveedor) {
        PncSalidaMaterialDto obj = service.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/enviarAprobar")
    public ResponseEntity<Object> enviarAprobacion(@RequestBody PncSalidaMaterialDto dto) {
        service.enviarAprobacion(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PncSalidaMaterialDto>> listarPorEstado(@PathVariable("estado") EstadoSalidaMaterial estado) {
        List<PncSalidaMaterialDto> obj = service.listarPorEstado(estado);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/aprobar")
    public ResponseEntity<Object> aprobarSalidaMaterial(@RequestBody PncSalidaMaterialDto dto) {
        service.aprobarSalidaMaterial(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/regresar")
    public ResponseEntity<Object> regresarSalidaMaterial(@RequestBody PncSalidaMaterialDto dto) {
        service.regresar(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/historial/{salidaId}")
    public ResponseEntity<List<PncHistorial>> listarHistorial(@PathVariable("salidaId") long salidaId) {
        List<PncHistorial> obj = historialService.buscarHistorialSalidaMaterial(salidaId);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{pncId}/{id}")
    public ResponseEntity<List<PncSalidaMaterialDto>> eliminar(@PathVariable("pncId") long pncId, @PathVariable("id") long id) {
        List<PncSalidaMaterialDto> salidas = service.eliminar(pncId, id);
        return ResponseEntity.ok(salidas);
    }

    @GetMapping("/catalogoDestino")
    public ResponseEntity<List<CatalogDTO>> obtenerDestinosFinal() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for (TipoDestino origen : TipoDestino.values()) {
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        catalgo.stream().sorted(Comparator.comparing(CatalogDTO::getLabel));
        return ResponseEntity.ok(catalgo);
    }

    @GetMapping("/catalogoEstados")
    public ResponseEntity<List<CatalogDTO>> obtenerEstados() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for (EstadoSalidaMaterial origen : EstadoSalidaMaterial.values()) {
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        catalgo.stream().sorted(Comparator.comparing(CatalogDTO::getLabel));
        return ResponseEntity.ok(catalgo);
    }

    @PostMapping("/actualizarInfoAdd")
    public ResponseEntity<Object> actualizarSalidaMaterialInfoAdd(@RequestBody PncSalidaMaterialInfoAdd dto) {
        PncSalidaMaterialInfoAdd obj = service.actualizarInfoAdd(dto);
        return ResponseEntity.ok(obj);
    }

}
