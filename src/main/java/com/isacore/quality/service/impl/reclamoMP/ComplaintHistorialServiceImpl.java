package com.isacore.quality.service.impl.reclamoMP;

import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintEstado;
import com.isacore.quality.model.reclamoMP.ComplaintHistorial;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;
import com.isacore.quality.repository.reclamoMP.ComplainDocumentoRepo;
import com.isacore.quality.repository.reclamoMP.ComplaintHistorialRepo;
import com.isacore.quality.service.reclamoMP.IComplaintHistorialService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@RequiredArgsConstructor
@Slf4j
@Service
public class ComplaintHistorialServiceImpl implements IComplaintHistorialService {

    private final ComplaintHistorialRepo historialRepo;
    private final IUserImptekRepo repoUsuario;
    private final ComplainDocumentoRepo documentoRepo;

    @Override
    public void agregar(Complaint salidaMaterial, ComplaintEstado estado, ComplaintOrdenFlujo ordenFlujo, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        ComplaintHistorial historial = new ComplaintHistorial(observacion, usuarioOp.get(), ordenFlujo, estado.toString(), salidaMaterial.getId());
        this.historialRepo.save(historial);
        log.info(String.format("Historial Pnc Salida Material guardado %s", historial));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComplaintHistorial> buscarHistorial(long reclamoId) {
        return historialRepo.findBySolicitudIdOrderByFechaRegistroAsc(reclamoId).stream().map(x -> {
            x.setTieneAdjuntos(documentoRepo.existsBySolicitudIdAndOrden(reclamoId, x.getOrden().name()));
            return x;
        }).collect(Collectors.toList());
    }
}
