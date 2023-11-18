package com.isacore.quality.controller.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.ConsultaDesviacionRequisitoDTO;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoDefecto;
import com.isacore.quality.model.pnc.PncDefecto;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/desviacion-requisito")
public class DesviacionRequisitoController {

    @Autowired
    private IDesviacionRequisitoService service;

    @GetMapping()
    public ResponseEntity<List<DesviacionRequisito>> listarTodos() {
        List<DesviacionRequisito> listadoDesviacionRequisito = service.findAll();

        return ResponseEntity.ok(listadoDesviacionRequisito);
    }

    @GetMapping("activos")
    public ResponseEntity<List<DesviacionRequisito>> listarDesviacionRequisitisActivos() {
        List<DesviacionRequisito> desviacionRequisitoActivas = service.listarDesviacionRequistoActivos();

        return ResponseEntity.ok(desviacionRequisitoActivas);
    }

    @PostMapping
    public ResponseEntity<DesviacionRequisito> crear(@RequestBody DesviacionRequisito desviacionRequisito) {
        DesviacionRequisito nuevaDesviacionRequisito = service.create(desviacionRequisito);

        return ResponseEntity.ok(nuevaDesviacionRequisito);
    }

    @PutMapping
    public ResponseEntity<DesviacionRequisito> modificar(@RequestBody DesviacionRequisito desviacionRequisito) {
        DesviacionRequisito desviacionRequisitoModificada = service.update(desviacionRequisito);

        return ResponseEntity.ok(desviacionRequisitoModificada);
    }

    @GetMapping("/{desviacionId}")
    public ResponseEntity<DesviacionRequisito> listarPorId(@PathVariable("desviacionId") Long desviacionId) {
        DesviacionRequisito desviacionRequisito = service.listarDesviacionRequisitosPorId(desviacionId);

        return ResponseEntity.ok(desviacionRequisito);
    }

    @PostMapping("/criterios")
    public ResponseEntity<Page<DesviacionRequisito>> listarPorCriterios(final Pageable page, @RequestBody final ConsultaDesviacionRequisitoDTO consulta) {
        final Page<DesviacionRequisito> resultadoConsulta = this.service.listar(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping(value = "/reporte/{desviacionId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarReporte(@PathVariable("desviacionId") Long id) {
        byte[] data = null;
        data = service.generarReporte(id);

        return ResponseEntity.ok(data);
    }

    @GetMapping("/agregarDefecto/{idDesviacion}/{idDefecto}")
    public ResponseEntity<List<DesviacionRequisitoDefecto>> agregarDefecto(@PathVariable("idDesviacion") long idDesviacion,  @PathVariable("idDefecto") long idDefecto) {
        List<DesviacionRequisitoDefecto> defectos = this.service.agregarDefecto(idDesviacion, idDefecto);
        return ResponseEntity.ok(defectos);
    }

    @DeleteMapping("/eliminarDefecto/{idDesviacion}/{idDefecto}")
    public ResponseEntity<List<DesviacionRequisitoDefecto>> eliminarDefecto(@PathVariable("idDesviacion") long idDesviacion,  @PathVariable("idDefecto") long idDefecto) {
        List<DesviacionRequisitoDefecto> defectos = this.service.eliminarDefecto(idDesviacion, idDefecto);
        return ResponseEntity.ok(defectos);
    }

}
