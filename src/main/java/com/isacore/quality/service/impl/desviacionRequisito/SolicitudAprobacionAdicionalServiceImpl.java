package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.SolicitudAprobacionAdicional;
import com.isacore.quality.model.desviacionRequisito.SolicitudAprobacionAdicionalEstado;
import com.isacore.quality.repository.SolicitudAprobacionAdicionalRepo;
import com.isacore.quality.service.desviacionRequisito.SolicitudAprobacionAdicionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitudAprobacionAdicionalServiceImpl implements SolicitudAprobacionAdicionalService {

    private final SolicitudAprobacionAdicionalRepo solicitudAprobacionAdicionalRepo;

    @Transactional
    @Override
    public String ejecutarAprobacion(long id, String uuid) {
        SolicitudAprobacionAdicional solicitud = solicitudAprobacionAdicionalRepo.findById(id).orElse(null);
        boolean continuar = true;
        String respuesta = "";
        if(solicitud == null){
            log.error("Solicitud aprobacion adicional id: {0} no encontrada ", id);
            continuar = false;
            respuesta = "Solicitud aprobacion adicional no encontrada";
        }

        if(!solicitud.getPrenda().equals(uuid)) {
            log.error("Solicitud aprobacion adicional id: {0} prenda no coincide {1} ", id, uuid);
            continuar = false;
            respuesta = "Solicitud aprobacion adicional no coincide con el hash de autenticación";
        }
        if(solicitud.getEstado().equals(SolicitudAprobacionAdicionalEstado.CONFIRMADO)) {
            log.error("Solicitud aprobacion adicional id: {0} ya fue procesada ", id);
            continuar = false;
            respuesta = "Solicitud aprobacion adicional ya ha sido procesada";
        }
        if(continuar){
            solicitud.setEstado(SolicitudAprobacionAdicionalEstado.CONFIRMADO);
            solicitud.getDesviacionAprobacionAdicional().setFechaAprobacion(LocalDateTime.now());
            solicitud.getDesviacionAprobacionAdicional().setActivo(false);
            //Posiblemente enviar notificación de la aprobacion.
            respuesta = "Solicitud aprobacion adicional procesada con éxito";
        }
        return  respuesta;
    }
}
