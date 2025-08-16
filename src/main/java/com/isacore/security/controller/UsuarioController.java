package com.isacore.security.controller;

import com.isacore.quality.model.Area;
import com.isacore.quality.service.IAreasService;
import com.isacore.security.dto.UsuarioDTO;
import com.isacore.security.dto.UsuarioPerfilDTO;
import com.isacore.security.model.RolEnum;
import com.isacore.security.model.UsuarioEstadoEnum;
import com.isacore.security.service.UsuarioService;
import com.isacore.util.CatalogDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final IAreasService servicioArea;

    public UsuarioController(UsuarioService usuarioService,
                             IAreasService servicioArea) {
        this.usuarioService = usuarioService;
        this.servicioArea = servicioArea;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody @Valid UsuarioDTO UsuarioDTO) {
        UsuarioDTO createdUser = usuarioService.crearUsuario(UsuarioDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable Long id) {
        UsuarioDTO user = usuarioService.listarUsuarioPorId(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/nombreUsuario/{nombreUsuario}/activo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> getUsuarioActivoPorNombreUsuario(@PathVariable String nombreUsuario) {
        UsuarioDTO user = usuarioService.obtenerUsuarioActivoPorNombreUsuario(nombreUsuario);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/activo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioDTO>> getAllActiveUsers() {
        List<UsuarioDTO> users = usuarioService.obtenerUsuariosActivos();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        List<UsuarioDTO> users = usuarioService.obtenerUsuarios();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody @Valid UsuarioDTO UsuarioDTO) {
        UsuarioDTO updatedUser = usuarioService.actualizarUsuario(UsuarioDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<CatalogDTO>> getRoles() {
        List<CatalogDTO> catalogo = Arrays.stream(RolEnum.values())
                .map(x -> new CatalogDTO(x.getDescripcion(), x.name())).collect(Collectors.toList());
        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<CatalogDTO>> getEstadosUsuarios() {
        List<CatalogDTO> catalogo = Arrays.stream(UsuarioEstadoEnum.values())
                .map(x -> new CatalogDTO(x.getDescripcion(), x.name())).collect(Collectors.toList());
        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/usuario-perfil/por-usuario/{userId}")
    public ResponseEntity<List<UsuarioPerfilDTO>> getRolesPorUsuarioId(@PathVariable long userId) {
        List<UsuarioPerfilDTO> roles = usuarioService.obtenerPerfilesPorUsuarioId(userId);
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/usuario-perfil")
    public ResponseEntity<UsuarioPerfilDTO> crearUsuarioPerfil(@RequestBody UsuarioPerfilDTO userProfileRequestDTO) {
        UsuarioPerfilDTO createdUserProfile = usuarioService.crearUsuarioPerfil(userProfileRequestDTO);
        return new ResponseEntity<>(createdUserProfile, HttpStatus.CREATED);
    }

    @DeleteMapping("/usuario-perfil/{usuarioId}/{perfilId}")
    public ResponseEntity<Map<String, Object>> eliminarUsuarioPerfil(
            @PathVariable long usuarioId,
            @PathVariable long perfilId) {
        Map<String, Object> result = usuarioService.eliminarPerfilPorUsuarioIdPerfilId(usuarioId, perfilId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listarAreas() {
        List<Area> areas = servicioArea.findAll();
        return ResponseEntity.ok(areas);
    }

}
