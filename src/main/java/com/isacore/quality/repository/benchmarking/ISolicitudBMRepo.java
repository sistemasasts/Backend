package com.isacore.quality.repository.benchmarking;

import com.isacore.quality.model.benchmarking.SolicitudBM;
import com.isacore.quality.model.se.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudBMRepo extends JpaRepository<SolicitudBM, Long> {

    List<SolicitudBM> findByNombreSolicitanteOrderByFechaCreacionDesc(String nombreSolicitante);

    List<SolicitudBM> findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(List<EstadoSolicitud> estados, String usuarioGestion);

    List<SolicitudBM> findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioValidador);

    List<SolicitudBM> findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioAprobador);

}
