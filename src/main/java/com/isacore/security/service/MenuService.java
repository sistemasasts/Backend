package com.isacore.security.service;

import com.isacore.security.dto.MenuDTO;
import com.isacore.security.exception.RecursoNotFoundException;
import com.isacore.security.exception.UsuarioPerfilNotFoundException;
import com.isacore.security.mapper.MenuMapper;
import com.isacore.security.model.*;
import com.isacore.security.repository.MenuPerfilRepositorio;
import com.isacore.security.repository.MenuRepositorio;
import com.isacore.security.repository.PerfilRepositorio;
import com.isacore.security.repository.UsuarioPerfilRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final UsuarioPerfilRepositorio usuarioPerfilRepositorio;
    private final MenuPerfilRepositorio menuPerfilRepositorio;
    private final MenuRepositorio menuRepositorio;
    private final MenuMapper menuMapper;
    private final PerfilRepositorio perfilRepositorio;

    public MenuService(
            UsuarioPerfilRepositorio usuarioPerfilRepositorio,
            MenuPerfilRepositorio menuPerfilRepositorio,
            MenuRepositorio menuRepositorio,
            MenuMapper menuMapper,
            PerfilRepositorio perfilRepositorio) {
        this.usuarioPerfilRepositorio = usuarioPerfilRepositorio;
        this.menuPerfilRepositorio = menuPerfilRepositorio;
        this.menuRepositorio = menuRepositorio;
        this.menuMapper = menuMapper;
        this.perfilRepositorio = perfilRepositorio;
    }

    @Transactional(readOnly = true)
    public List<MenuDTO> getMenusPorUsuarioNombre(String nombreUsuario) {

        List<Menu> menus = new ArrayList<>();
        List<Menu> submenu = new ArrayList<>();
        List<UsuarioPerfil> list = this.usuarioPerfilRepositorio.findByUsuario_NombreUsuario(nombreUsuario);
        if (list.isEmpty()) {
            throw new UsuarioPerfilNotFoundException("Usuario: " + nombreUsuario + " no tiene perfil asigando.");
        }
        list.forEach(ele -> {
            List<MenuPerfil> menusProfile = this.menuPerfilRepositorio.findByPerfil(ele.getPerfil());
            menusProfile.forEach(m -> {
                if (m.getMenu().getTipo().equals(MenuTipoEnum.MENU)) {
                    menus.add(m.getMenu());
                } else {
                    submenu.add(m.getMenu());
                }
            });
            menus.forEach(e -> {
                List<Menu> submenusTmp = submenu
                        .stream()
                        .filter(c -> Objects.equals(c.getPadreMenu().getId(), e.getId()))
                        .collect(Collectors.toList());
                e.setMenus(submenusTmp);
            });
        });

        menus.sort(Comparator.comparing(Menu::getEtiqueta));
        return menuMapper.toResponseDTOList(menus);
    }

    public List<MenuDTO> obtenerPermisosPorUsuario(String userName) {
        List<Menu> menus = menuRepositorio.getMenusByNombreUsuario(userName);
        if (menus.isEmpty()) {
            throw new RecursoNotFoundException("El usuario " + userName + " no tiene permisos asignados.");
        }
        List<Menu> subMenu = new ArrayList<>();
        menus.forEach(mp -> {
            if (mp.getPadreMenu() != null)
                subMenu.add(mp);
        });
        menus.removeAll(subMenu);
        menus.forEach(mp -> {
            mp.setMenus(subMenu.stream().filter(sp -> Objects.equals(sp.getPadreMenu().getId(), mp.getId())).collect(Collectors.toList()));
        });
        return menuMapper.toResponseDTOList(menus);
    }

    @Transactional(readOnly = true)
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepositorio.findAll();
        return menuMapper.toResponseDTOList(menus);
    }

    @Transactional(readOnly = true)
    public List<MenuDTO> getMenusPorPerfilId(Long profileId) {
        List<MenuPerfil> permissions = menuPerfilRepositorio.findByPerfilId(profileId);
        List<Menu> menus = permissions.stream().map(MenuPerfil::getMenu).collect(Collectors.toList());
        return menuMapper.toResponseDTOList(menus);
    }

    @Transactional
    public boolean asignarMenusPorPerfil(long perfilId, List<MenuDTO> menusDto) {
        Perfil profile = perfilRepositorio.findById(perfilId)
                .orElseThrow(() -> new RecursoNotFoundException("Perfil no encontrado con ID: " + perfilId));

        List<Long> menusIds = menusDto.stream().map(MenuDTO::getId).collect(Collectors.toList());
        List<Menu> menusToAssign = menuRepositorio.findAllById(menusIds);
        List<MenuPerfil> permissions = menuPerfilRepositorio.findByPerfil(profile);
        if (!permissions.isEmpty()) {
            menuPerfilRepositorio.deleteAll(permissions);
        }

        List<MenuPerfil> nuevosMenuProfiles = new ArrayList<>();
        menusToAssign.forEach(menu -> {
            MenuPerfil menuProfile = new MenuPerfil();
            menuProfile.setPerfilId(profile.getId());
            menuProfile.setMenuId(menu.getId());
            menuProfile.setActivo(true);
            nuevosMenuProfiles.add(menuProfile);
        });
        menuPerfilRepositorio.saveAll(nuevosMenuProfiles);
        return true;
    }
}
