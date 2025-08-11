package com.isacore.security.repository;

import com.isacore.security.model.Menu;
import com.isacore.security.model.MenuTipoEnum;
import com.isacore.security.model.Usuario;
import com.isacore.security.model.UsuarioEstadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepositorio extends JpaRepository<Menu, Long> {

    List<Menu> findByTipo(MenuTipoEnum tipoMenu);

    List<Menu> findByPadreId(Integer padreId);

    @Query(value= "SELECT DISTINCT m.*" +
            "FROM menu m "+
            "INNER JOIN menu_perfil mp ON mp.menu_id = m.id "+
            "INNER JOIN perfil p ON p.id = mp.perfil_id "+
            "INNER JOIN usuario_perfil up ON up.perfil_id = p.id "+
            "INNER JOIN usuario u ON u.id = up.usuario_id "+
            "WHERE u.nombre_usuario = :usuarioNombre "+
            "ORDER BY m.orden_menu"
            ,nativeQuery = true  )
    List<Menu> getMenusByNombreUsuario(@Param("usuarioNombre") String usuarioNombre);
}
