package com.isacore.quality.controller.pnc;


import com.isacore.quality.model.pnc.EstadoSalidaMaterial;
import com.isacore.quality.model.pnc.PncDocumento;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.service.pnc.IPncDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/pncDocumentos")
public class PncDocumentoController {

    @Autowired
    private IPncDocumentoService service;

    @GetMapping("/{estado}/{orden}/{salidaId}")
    public ResponseEntity<List<PncDocumento>> listarArchivos(@PathVariable("estado") EstadoSalidaMaterial estado,
                                                                           @PathVariable("orden") PncOrdenFlujo orden,
                                                                           @PathVariable("salidaId") Long salidaId) throws IOException {
        List<PncDocumento> files = service.buscarPorEstadoYOrdenYSalidaId(estado, orden, salidaId);
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
        PncDocumento imgBase64 = service.subirArchivo(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(imgBase64);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPP(@PathVariable("id") Long id) {
        boolean resultado = service.eliminarDocumento(id);
        return ResponseEntity.ok(resultado);
    }

}
