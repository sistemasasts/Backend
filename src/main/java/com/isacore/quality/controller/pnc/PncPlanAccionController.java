package com.isacore.quality.controller.pnc;


import com.isacore.quality.model.pnc.EstadoPncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.quality.service.pnc.IPncPlanAccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pncPlanesAccion")
public class PncPlanAccionController {

    @Autowired
    private IPncPlanAccionService service;

    @GetMapping("/listarPorSalidaMaterial/{salidaMaterialId}")
    public ResponseEntity<List<PncPlanAccionDto>> listarPorSalidaMaterialId(@PathVariable("salidaMaterialId") long salidaMaterialId) {
        List<PncPlanAccionDto> planes = service.listarPorSalidaMaterialId(salidaMaterialId);
        return ResponseEntity.ok(planes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PncPlanAccionDto> listarPorId(@PathVariable("id") long id) {
        PncPlanAccionDto planes = service.listarPorId(id);
        return ResponseEntity.ok(planes);
    }

    @PostMapping
    public ResponseEntity<List<PncPlanAccionDto>> crear(@RequestBody PncPlanAccionDto dto) {
        List<PncPlanAccionDto> proveedorCreado = service.registrar(dto);
        return ResponseEntity.ok(proveedorCreado);
    }

    @PutMapping
    public ResponseEntity<List<PncPlanAccionDto>> modificar(@RequestBody PncPlanAccionDto proveedor) {
        List<PncPlanAccionDto> obj = service.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }

    @DeleteMapping("/{salidaMaterialId}/{planAccionId}")
    public ResponseEntity<List<PncPlanAccionDto>> eliminar(@PathVariable("salidaMaterialId") long salidaMaterialId,
                                                           @PathVariable("planAccionId") long planAccionId) {
        List<PncPlanAccionDto> obj = service.eliminar(planAccionId, salidaMaterialId);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/procesar")
    public ResponseEntity<Object> procesar(@RequestBody PncPlanAccionDto dto) {
        service.procesar(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/listarPorEstado/{estado}")
    public ResponseEntity<List<PncPlanAccionDto>> listarPorEstado(@PathVariable("estado") EstadoPncPlanAccion estado) {
        List<PncPlanAccionDto> planes = service.listarPorEstado(estado);
        return ResponseEntity.ok(planes);
    }

}
