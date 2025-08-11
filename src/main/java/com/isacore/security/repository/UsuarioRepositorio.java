package com.isacore.security.repository;

import com.isacore.security.model.Usuario;
import com.isacore.security.model.UsuarioEstadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuairo);

    List<Usuario> findByEstado(UsuarioEstadoEnum estado);
}
