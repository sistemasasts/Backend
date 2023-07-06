package com.isacore.quality.service.impl.pnc;

import com.isacore.quality.model.pnc.PncHistorial;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.repository.pnc.IPncDocumentoRepo;
import com.isacore.quality.repository.pnc.IPncHistorialRepo;
import com.isacore.quality.service.pnc.IPncHistorialService;
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
public class PncHistorialServiceImpl implements IPncHistorialService {

    private final IPncHistorialRepo historialRepo;
    private final IUserImptekRepo repoUsuario;
    private final IPncDocumentoRepo documentoRepo;

    @Override
    public void agregar(PncSalidaMaterial salidaMaterial, PncOrdenFlujo ordenFlujo, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        PncHistorial historial = new PncHistorial(observacion, usuarioOp.get(), salidaMaterial.getId(), null, salidaMaterial.getEstado(), ordenFlujo);
        this.historialRepo.save(historial);
        log.info(String.format("Historial Pnc Salida Material guardado %s", historial));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncHistorial> buscarHistorialSalidaMaterial(long salidaMaterialId) {
        return historialRepo.findBySalidaMaterialIdOrderByFechaRegistroAsc(salidaMaterialId).stream().map(x -> {
            x.setTieneAdjuntos(documentoRepo.existsBySalidaMaterialIdAndEstadoAndOrdenFlujo(salidaMaterialId, x.getEstadoSalidaMaterial(), x.getOrden()));
            return x;
        }).collect(Collectors.toList());
    }
}
