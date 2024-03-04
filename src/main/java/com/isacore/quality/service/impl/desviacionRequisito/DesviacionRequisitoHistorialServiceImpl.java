package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoHistorial;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoOrdenFlujo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoDocumentoRepo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoHistorialRepo;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoHistorialService;
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
public class DesviacionRequisitoHistorialServiceImpl implements IDesviacionRequisitoHistorialService {

    private final IDesviacionRequisitoHistorialRepo historialRepo;
    private final IUserImptekRepo repoUsuario;
    private final IDesviacionRequisitoDocumentoRepo documentoRepo;

    @Override
    public void agregar(DesviacionRequisito salidaMaterial, DesviacionRequisitoOrdenFlujo ordenFlujo, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        DesviacionRequisitoHistorial historial = new DesviacionRequisitoHistorial(observacion, usuarioOp.get(),
                salidaMaterial, ordenFlujo, salidaMaterial.getEstado());
        this.historialRepo.save(historial);
        log.info(String.format("Historial Pnc Salida Material guardado %s", historial));
    }

    @Transactional(readOnly = true)
    @Override
    public List<DesviacionRequisitoHistorial> buscarHistorial(long id) {
        return historialRepo.findByDesviacionRequisito_IdOrderByFechaRegistroAsc(id).stream().map(x -> {
            x.setTieneAdjuntos(documentoRepo.existsByDesviacionRequisito_IdAndOrden(id, x.getOrden()));
            return x;
        }).collect(Collectors.toList());
    }
}
