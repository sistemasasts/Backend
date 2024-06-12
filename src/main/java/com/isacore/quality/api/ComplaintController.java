package com.isacore.quality.api;

import com.isacore.quality.model.desviacionRequisito.EstadoDesviacion;
import com.isacore.quality.model.reclamoMP.*;
import com.isacore.quality.service.reclamoMP.IComplaintHistorialService;
import com.isacore.quality.service.reclamoMP.IComplaintService;
import com.isacore.util.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/complaints")
public class ComplaintController {

    @Autowired
    private IComplaintService service;

    @Autowired
    private IComplaintHistorialService historialService;

    @PostMapping("/close")
    public ResponseEntity<Object> closeComplaint(@RequestBody ComplaintDto dto) {
        service.close(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/listarPorCriterios")
    public ResponseEntity<Page<ComplaintDto>> closeComplaint(Pageable page, @RequestBody ConsultaReclamoDto criterios) {
        Page<ComplaintDto> compraDto = service.listarPorCriterio(page, criterios);
        return ResponseEntity.ok(compraDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listarReclamoPorId(@PathVariable("id") long id) {
        ComplaintDto compraDto = service.buscarPorId(id);
        return ResponseEntity.ok(compraDto);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Object> registrarReclamo(@RequestBody ComplaintDto dto) {
        ComplaintDto compraDto = service.create(dto);
        return ResponseEntity.ok(compraDto);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Object> actualizarReclamo(@RequestBody ComplaintDto dto) {
        ComplaintDto compraDto = service.update(dto);
        return ResponseEntity.ok(compraDto);
    }


    @PostMapping("/crearProblema")
    public ResponseEntity<Object> crearProblema(@RequestPart("info") String info, @RequestPart("file") MultipartFile file) throws IOException {
        List<ProblemDto> problemas = service.agregarProblema(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(problemas);
    }

    @GetMapping("/listarProblemas/{id}")
    public ResponseEntity<Object> listarProblemasPorReclamoId(@PathVariable("id") long id) {
        List<ProblemDto> compraDto = service.listarProblemas(id);
        return ResponseEntity.ok(compraDto);
    }

    @DeleteMapping("/eliminarProblema/{reclamoId}/{problemaId}")
    public ResponseEntity<Object> eliminarProblema(@PathVariable("reclamoId") long reclamoId, @PathVariable("problemaId") long problemaId) {
        List<ProblemDto> problemas = service.eliminarProblema(reclamoId, problemaId);
        return ResponseEntity.ok(problemas);
    }

    @PostMapping("/enviar")
    public ResponseEntity<Object> enviar(@RequestBody ComplaintDto dto) {
        service.enviarReclamo(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/procesarCalidad")
    public ResponseEntity<Object> procesarCalidad(@RequestBody ComplaintDto dto) {
        service.procesarReclamoCalidad(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/procesarCompras")
    public ResponseEntity<Object> procesarCompras(@RequestBody ComplaintDto dto) {
        service.procesarReclamoCompras(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/anular")
    public ResponseEntity<Object> anular(@RequestBody ComplaintDto dto) {
        service.anular(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/listarAsignadasPorEstado/{estado}")
    public ResponseEntity<Object> listarAsignadasPorEstado(@PathVariable("estado") ComplaintEstado estado) {
        List<ComplaintDto> compraDto = service.listarAsignadasPorEstado(estado);
        return ResponseEntity.ok(compraDto);
    }

    @GetMapping("/historial/{id}")
    public ResponseEntity<Object> listarHistorial(@PathVariable("id") long id) {
        List<ComplaintHistorial> obj = historialService.buscarHistorial(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value="/reporte/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarReporte(@PathVariable("id") long id) {
        byte [] data = null;
        data= service.generateReporte(id);
        return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
    }

    @PostMapping("/crearAccionEjecutada")
    public ResponseEntity<Object> crearAccionEjecutada(@RequestBody ExecutedActionDto dto) {
        List<ExecutedActionDto> problemas = service.agregarAccionEjecutada(dto);
        return ResponseEntity.ok(problemas);
    }

    @PutMapping("/actualizarAccionEjecutada")
    public ResponseEntity<Object> actualizarAccionEjecutada(@RequestBody ExecutedActionDto dto) {
        List<ExecutedActionDto> problemas = service.actualizarAccionEjecutada(dto);
        return ResponseEntity.ok(problemas);
    }

    @DeleteMapping("/eliminarAccionEjecutada/{reclamoId}/{problemaId}")
    public ResponseEntity<Object> eliminarAccionEjecutada(@PathVariable("reclamoId") long reclamoId, @PathVariable("problemaId") long problemaId ) {
        List<ExecutedActionDto> problemas = service.eliminarAccionEjecutada(reclamoId, problemaId);
        return ResponseEntity.ok(problemas);
    }

    @PostMapping("/crearPlanAccion")
    public ResponseEntity<Object> crearPlanAccion(@RequestBody ProviderActionPlanDto dto) {
        List<ProviderActionPlanDto> problemas = service.agregarPLanAccion(dto);
        return ResponseEntity.ok(problemas);
    }

    @PutMapping("/actualizarPlanAccion")
    public ResponseEntity<Object> actualizarPlanAccion(@RequestBody ProviderActionPlanDto dto) {
        List<ProviderActionPlanDto> problemas = service.actualizarPLanAccion(dto);
        return ResponseEntity.ok(problemas);
    }

    @PostMapping("/eliminarPlanAccion/{reclamoId}/{planId}")
    public ResponseEntity<Object> eliminarPlanAccion(@PathVariable("reclamoId") long reclamoId, @PathVariable("planId") long planId, @RequestBody ProviderActionPlanDto dto ) {
        List<ProviderActionPlanDto> problemas = service.eliminarPlanAccion(reclamoId, planId, dto);
        return ResponseEntity.ok(problemas);
    }

    @PostMapping("/notificarReclamo")
    public ResponseEntity<Object> notificarReclamo(@RequestBody ComplaintDto dto) {
        service.notificarReclamo(dto);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/enviarPlanesAccion")
    public ResponseEntity<Object> enviarPlanesAccion(@RequestBody ComplaintDto dto) {
        service.enviarPlanesAccion(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/listarPlanesAccionAsignadasPorEstado")
    public ResponseEntity<Object> listarPlanesAccionAsignadasPorEstado() {
        List<ComplaintDto> compraDto = service.listarPorPlanerAccionPorUsuarioSesion();
        return ResponseEntity.ok(compraDto);
    }

    @PostMapping("/procesarPlanAccion")
    public ResponseEntity<Object> procesarPlanAccion(@RequestBody ProviderActionPlanDto dto) {
        List<ProviderActionPlanDto> problemas = service.procesarPlanAccion(dto);
        return ResponseEntity.ok(problemas);
    }

    @PostMapping("/validarPlanAccion")
    public ResponseEntity<Object> validarPlanAccion(@RequestBody ProviderActionPlanDto dto) {
        List<ProviderActionPlanDto> problemas = service.validarPlanAccion(dto);
        return ResponseEntity.ok(problemas);
    }

    @GetMapping("/catalogoEstado")
    public ResponseEntity<List<CatalogDTO>> obtenerEstados() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for (ComplaintEstado origen : Arrays.stream(ComplaintEstado.values()).sorted().collect(Collectors.toList())) {
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        return ResponseEntity.ok(catalgo);
    }


}
