package com.isacore.security.controller;

import com.isacore.security.dto.MenuDTO;
import com.isacore.security.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping(value = "/por-usuario/{userName}")
    public ResponseEntity<List<MenuDTO>> getMenusPorUsuario(
            @RequestParam(required = true) @PathVariable String userName) {
        List<MenuDTO> menus = menuService.getMenusPorUsuarioNombre(userName);
        return ResponseEntity.ok(menus);
    }

    @GetMapping(value = "/permisos/por-usuario/{userName}")
    public ResponseEntity<List<MenuDTO>> getPermisosPorUsuario(
            @PathVariable String userName) {
        List<MenuDTO> permisos = menuService.obtenerPermisosPorUsuario(userName);
        return ResponseEntity.ok(permisos);
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    @GetMapping(value = "/por-perfil/{perfilId}")
    public ResponseEntity<List<MenuDTO>> getMenusPorPerfilId(
            @PathVariable Long perfilId) {
        List<MenuDTO> menus = menuService.getMenusPorPerfilId(perfilId);
        return ResponseEntity.ok(menus);
    }

    @PostMapping(value = "/menu-profile/asignar/{perfilId}")
    public ResponseEntity<Boolean> asignarMenusPorPerfil(
            @PathVariable long perfilId,
            @Valid @RequestBody List<MenuDTO> menusDto) {
        boolean success = menuService.asignarMenusPorPerfil(perfilId, menusDto);
        return ResponseEntity.ok(success);
    }

}
