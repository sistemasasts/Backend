package com.isacore.quality.controller.reclamoMP;


import com.isacore.quality.model.reclamoMP.ComplaintDocumento;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;
import com.isacore.quality.service.reclamoMP.IComplaintDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/reclamoMPDocumentos")
public class ComplaintDocumentoController {

    @Autowired
    private IComplaintDocumentoService service;

    @GetMapping("/{orden}/{salidaId}")
    public ResponseEntity<Object> listarArchivos(
            @PathVariable("orden") ComplaintOrdenFlujo orden,
            @PathVariable("salidaId") Long salidaId) {
        List<ComplaintDocumento> files = service.buscarPorOrdenYReclamoId(orden, salidaId);
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = "/ver/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> ver(@PathVariable("id") Long id) {
        byte[] data = null;
        data = service.descargar(id);
        return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
    }

    @PostMapping("/subir")
    public ResponseEntity<Object> subirArchivo(@RequestPart("info") String info, @RequestPart("file") MultipartFile file) throws IOException {
        ComplaintDocumento imgBase64 = service.subirArchivo(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(imgBase64);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPP(@PathVariable("id") Long id) {
        boolean resultado = service.eliminarDocumento(id);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping(value = "/comprimido/{historialId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> descargarComprimido(@PathVariable("historialId") Long id) {
        byte[] data = null;
        data = service.descargarPorHistorialId(id);
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

}
