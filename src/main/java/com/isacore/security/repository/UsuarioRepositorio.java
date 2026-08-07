package com.isacore.security.repository;

import com.isacore.security.model.Usuario;
import com.isacore.security.model.UsuarioEstadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuairo);

    List<Usuario> findByEstado(UsuarioEstadoEnum estado);

    @Query(value = "SELECT\n" +
            "    *\n" +
            "FROM usuario u\n" +
            "INNER JOIN area a\n" +
            "    ON a.area_id = u.area_id\n" +
            "WHERE a.area_id = 6\n" +
            "  AND u.estado = 'ACTIVO';", nativeQuery = true)
    List<Usuario> findByActivoAndAreaComprasLogistica();
}
