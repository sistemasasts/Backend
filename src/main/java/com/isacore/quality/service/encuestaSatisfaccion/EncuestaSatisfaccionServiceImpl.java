package com.isacore.quality.service.encuestaSatisfaccion;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudEvaluacion.EncuestaSatisfaccion;
import com.isacore.quality.repository.encuestaSatisfaccion.IEncuestaSatisfaccionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Slf4j
@Service
public class EncuestaSatisfaccionServiceImpl {

    private IEncuestaSatisfaccionRepo repo;

    @Autowired
    public EncuestaSatisfaccionServiceImpl(IEncuestaSatisfaccionRepo repo) {
        this.repo = repo;
    }

    public void registrar(EncuestaSatisfaccion dto){
        EncuestaSatisfaccion encuesta = new EncuestaSatisfaccion(
            dto.getSolicitudId(),
            dto.getTipoSolicitud(),
            dto.getSatisfaccion(),
            dto.getComentario(),
            nombreUsuarioEnSesion());
        this.repo.save(encuesta);
        log.info(String.format("Encuesta de satisfaccion registrada %s", encuesta));
    }

    public boolean existeEncuestaParaSolicitud(TipoSolicitud tipo, long solicitudId){
        return this.repo.existsByTipoSolicitudAndSolicitudId(tipo, solicitudId);
    }
}
