package com.isacore.quality.repository.encuestaSatisfaccion;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudEvaluacion.EncuestaSatisfaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEncuestaSatisfaccionRepo extends JpaRepository<EncuestaSatisfaccion, Long>{

    boolean existsByTipoSolicitudAndSolicitudId(TipoSolicitud tipoSolicitud, long solicitudId);
}
