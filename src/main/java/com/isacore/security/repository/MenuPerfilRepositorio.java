package com.isacore.security.repository;

import com.isacore.security.model.MenuPerfil;
import com.isacore.security.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuPerfilRepositorio extends JpaRepository<MenuPerfil, Long> {

    List<MenuPerfil> findByPerfilId(Long perfilId);

    List<MenuPerfil> findByPerfil(Perfil perfil);
}
