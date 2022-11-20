package com.isacore.quality.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.TestCriterioDTO;
import com.isacore.quality.model.Test;
import com.isacore.quality.model.VistaTest;
import com.isacore.quality.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/tests")
public class TestControlador {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    @Autowired
    private ITestService servicio;

    @PostMapping("/consulta")
    public ResponseEntity<Page<VistaTest>> consultarCotizaciones(final Pageable page, @RequestBody final TestCriterioDTO consulta) {
        final Page<VistaTest> resultadoConsulta = this.servicio.consultarPorCriterio(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping(value="/reporte/{parametros}",  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarReporte(@PathVariable("parametros") String parametros) throws JsonProcessingException {
        TestCriterioDTO criterios = JSON_MAPPER.readValue(parametros, TestCriterioDTO.class);
        byte[] resultadoConsulta = this.servicio.generarReporte(criterios);
        return ResponseEntity.ok(resultadoConsulta);
    }
}
