package com.isacore.security.controller;


import com.isacore.security.dto.PerfilDTO;
import com.isacore.security.service.PerfilService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/perfiles")
public class PerfilController {
    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PerfilDTO>> getAllProfiles() {
        List<PerfilDTO> profiles = perfilService.listar();
        return ResponseEntity.ok(profiles);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilDTO> crearPerfil(@Valid @RequestBody PerfilDTO PerfilDTO) {
        PerfilDTO createProfile = perfilService.crearPerfil(PerfilDTO);
        return ResponseEntity.ok(createProfile);
    }

    @GetMapping(value = "/activo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PerfilDTO>> obtenerPerfilesActivos() {
        List<PerfilDTO> profiles = perfilService.obtenerPerfilesActivos();
        return ResponseEntity.ok(profiles);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilDTO> updateProfile(
            @Valid @RequestBody PerfilDTO PerfilDTO) {
        PerfilDTO updatedProfile = perfilService.actualizarPerfil(PerfilDTO);
        return ResponseEntity.ok(updatedProfile);
    }

}
