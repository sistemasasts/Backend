package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.EstadoSolicitudPPResponsable;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoResponsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ISolicitudPruebaProcesoResponsableRepo extends JpaRepository<SolicitudPruebaProcesoResponsable, Long> {

    List<SolicitudPruebaProcesoResponsable> findByUsuarioResponsableAndActivoTrueAndOrdenAndEstadoIn(String usuarioResponsable, OrdenFlujoPP orden,
                                                                                                     Collection<EstadoSolicitudPPResponsable> estados);

    List<SolicitudPruebaProcesoResponsable> findByUsuarioAsignadoAndActivoTrueAndOrdenAndEstadoIn(String usuarioResponsable, OrdenFlujoPP orden,
                                                                                                  Collection<EstadoSolicitudPPResponsable> estados);

    List<SolicitudPruebaProcesoResponsable> findByOrdenAndEstadoInAndSolicitudPruebasProceso_Id(OrdenFlujoPP orden,
                                                                                                Collection<EstadoSolicitudPPResponsable> estados,
                                                                                                long solicitudId);

    List<SolicitudPruebaProcesoResponsable> findBySolicitudPruebasProceso_Id(long solicitudId);

}
