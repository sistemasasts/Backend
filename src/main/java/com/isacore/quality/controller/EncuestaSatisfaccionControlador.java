package com.isacore.quality.controller;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudEvaluacion.EncuestaSatisfaccion;
import com.isacore.quality.model.solicitudEvaluacion.EscalaSatisfaccion;
import com.isacore.quality.model.spp.ConfiguracionFlujoPruebaProceso;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.service.encuestaSatisfaccion.EncuestaSatisfaccionServiceImpl;
import com.isacore.quality.service.spp.IConfiguracionFlujoPruebaProceso;
import com.isacore.util.CatalogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/encuestaSatisfaccion")
public class EncuestaSatisfaccionControlador {

	@Autowired
	private EncuestaSatisfaccionServiceImpl servicio;

	@GetMapping("/existeEncuesta/{tipo}/{solicitudId}")
    public ResponseEntity<Object> existeEncuestaRegistrada(@PathVariable("tipo") TipoSolicitud tipo, @PathVariable("solicitudId") long solicitudId) {
        final boolean respuesta = this.servicio.existeEncuestaParaSolicitud(tipo, solicitudId);
        return ResponseEntity.ok(respuesta);
    }

	@PostMapping
	public ResponseEntity<Object> crear(@RequestBody EncuestaSatisfaccion obj) {
        servicio.registrar(obj);
		return ResponseEntity.ok(true);
	}

    @GetMapping("/escalaSatisfaccion")
    public ResponseEntity<List<CatalogDTO>> listarEscalaSatisfaccion() {
        final List<CatalogDTO> lista = Arrays.asList(EscalaSatisfaccion.values()).stream().map(x -> {
            return new CatalogDTO(x.toString(), x.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}
