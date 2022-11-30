package com.isacore.quality.repository.se;

import com.isacore.quality.model.se.SolicitudEnsayoAdjuntoRequerido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISolicitudEnsayoAdjuntoRequeridoRepo extends JpaRepository<SolicitudEnsayoAdjuntoRequerido, Long> {

    Optional<SolicitudEnsayoAdjuntoRequerido> findByDocumentoId(long documentoId);
}
