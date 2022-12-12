package com.isacore.quality.repository.se;

import java.util.List;

import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.SolicitudEnsayo;

@Repository
public interface ISolicitudEnsayoRepo extends JpaRepository<SolicitudEnsayo, Long> {

	List<SolicitudEnsayo> findByNombreSolicitanteOrderByFechaCreacionDesc(String nombreSolicitante);

	List<SolicitudEnsayo> findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(List<EstadoSolicitud> estados, String usuarioGestion);

	List<SolicitudEnsayo> findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioValidador);

	List<SolicitudEnsayo> findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioAprobador);

    List<SolicitudEnsayo> findBySolicitudPadreId(long solicitudPadreId);

    @Query(value = "WITH TreeCTE AS\n" +
        "(\n" +
        "SELECT id, codigo, solicitud_padre_id\n" +
        "FROM solicitud_ensayo\n" +
        "where id= :solicitudId\n" +
        "UNION ALL\n" +
        "SELECT Tre.id, Tre.codigo, Tre.solicitud_padre_id\n" +
        "FROM TreeCTE AS TreCTE\n" +
        "JOIN solicitud_ensayo AS Tre\n" +
        "ON Tre.id = TreCTE.solicitud_padre_id\n" +
        ")\n" +
        "SELECT * FROM TreeCTE ", nativeQuery = true)
    List<Object[]> obtenerSolicitudesHija(@Param("solicitudId")long solicitudId);
}
