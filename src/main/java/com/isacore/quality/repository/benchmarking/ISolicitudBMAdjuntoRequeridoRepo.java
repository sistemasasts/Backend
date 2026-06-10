package com.isacore.quality.repository.benchmarking;

import com.isacore.quality.model.benchmarking.SolicitudBMAdjuntoRequerido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISolicitudBMAdjuntoRequeridoRepo extends JpaRepository<SolicitudBMAdjuntoRequerido, Long> {

    Optional<SolicitudBMAdjuntoRequerido> findByDocumentoId(long documentoId);
}
