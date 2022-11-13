package com.isacore.quality.controller;

import com.isacore.quality.dto.TestCriterioDTO;
import com.isacore.quality.model.VistaTest;
import com.isacore.quality.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tests")
public class TestControlador {

    @Autowired
    private ITestService servicio;

    @PostMapping("/consulta")
    public ResponseEntity<Page<VistaTest>> consultarCotizaciones(final Pageable page, @RequestBody final TestCriterioDTO consulta) {
        final Page<VistaTest> resultadoConsulta = this.servicio.consultarPorCriterio(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }
}
