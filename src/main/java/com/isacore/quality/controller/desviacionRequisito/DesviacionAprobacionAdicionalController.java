package com.isacore.quality.controller.desviacionRequisito;


import com.isacore.quality.service.desviacionRequisito.SolicitudAprobacionAdicionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/aprobacionAdicional")
public class DesviacionAprobacionAdicionalController {

    @Autowired
    private SolicitudAprobacionAdicionalService service;

    @GetMapping("/{id}/{uuid}")
    public ResponseEntity<Object> ejecutarAprobacion(
            @PathVariable("id") long id,
            @PathVariable("uuid") String uuid) {
        String respuesta = service.ejecutarAprobacion(id, uuid);
        return ResponseEntity.ok(respuesta);
    }

}
