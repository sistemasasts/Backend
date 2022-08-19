package com.isacore.quality.repository.se;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.Secuencial;
import com.isacore.quality.model.se.TipoSolicitud;

@Repository
public interface ISecuencialRepo extends JpaRepository<Secuencial, Long> {

	Optional<Secuencial> findByTipoSolicitudAndActivoTrue(TipoSolicitud tipoSolicitud);
}
