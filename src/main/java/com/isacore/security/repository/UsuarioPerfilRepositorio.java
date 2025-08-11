package com.isacore.security.repository;

import com.isacore.security.model.RolEnum;
import com.isacore.security.model.UsuarioEstadoEnum;
import com.isacore.security.model.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioPerfilRepositorio extends JpaRepository<UsuarioPerfil, Long> {

    List<UsuarioPerfil> findByUsuarioId(long usuarioId);

    Optional<UsuarioPerfil> findByUsuarioIdAndPerfilId(long usuarioId, long perfilId);

    List<UsuarioPerfil> findByUsuario_NombreUsuario(String nombreUsuario);

    List<UsuarioPerfil> findByPerfil_RolAndUsuario_Estado(RolEnum role, UsuarioEstadoEnum estado);

    List<UsuarioPerfil> findByPerfil_RolInAndUsuario_Estado(Collection<RolEnum> roles, UsuarioEstadoEnum estado);

}
