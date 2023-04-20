package com.isacore.quality.service.impl.se;

import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.HistorialPPCompletoDto;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.quality.repository.se.ISolicitudDocumentoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.service.se.ISolicitudHistorialService;
import com.isacore.quality.service.spp.ISolicitudPruebaProcesoHistorialService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class SolicitudHistorialServiceImpl implements ISolicitudHistorialService {

    private static final Log LOG = LogFactory.getLog(SolicitudHistorialServiceImpl.class);

    private ISolicitudHistorialRepo repo;
    private ISolicitudDocumentoRepo repoDocumento;
    private ISolicitudEnsayoRepo solicitudEnsayoRepo;
    private ISolicitudPruebaProcesoHistorialService solicitudPruebaProcesoHistorialService;

    @Autowired
    public SolicitudHistorialServiceImpl(
            ISolicitudHistorialRepo repo,
            ISolicitudDocumentoRepo repoDocumento,
            ISolicitudEnsayoRepo solicitudEnsayoRepo,
            ISolicitudPruebaProcesoHistorialService solicitudPruebaProcesoHistorialService) {
        this.repo = repo;
        this.repoDocumento = repoDocumento;
        this.solicitudEnsayoRepo = solicitudEnsayoRepo;
        this.solicitudPruebaProcesoHistorialService = solicitudPruebaProcesoHistorialService;
    }

    @Override
    public List<SolicitudHistorial> findAll() {
        return repo.findAll();
    }

    @Override
    public SolicitudHistorial create(SolicitudHistorial obj) {

        LOG.info(String.format("Historial a guardar %s", obj));
        return repo.save(obj);
    }

    @Override
    public SolicitudHistorial findById(SolicitudHistorial id) {
        return repo.findById(id.getId()).orElse(null);
    }

    @Override
    public SolicitudHistorial update(SolicitudHistorial obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<SolicitudHistorial> buscarHistorial(long solicitudId) {
        String usuarioSesion = nombreUsuarioEnSesion();
        List<SolicitudHistorial> historial = repo.findBySolicitudEnsayo_IdOrderByFechaRegistroAsc(solicitudId);
        int numeroRegistros = (int) historial.stream().filter(x -> x.getOrden().equals(OrdenFlujo.APROBAR_INFORME)).count();
        int count = 0;
        for (SolicitudHistorial x : historial) {
            if (x.getSolicitudEnsayo().getNombreSolicitante().equals(usuarioSesion)) {
                switch (x.getOrden()) {
                    case INGRESO_SOLICITUD:
                        x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
                        break;
                    case APROBAR_INFORME:
                        count += 1;
                        if (!(x.getSolicitudEnsayo().getEstado().equals(EstadoSolicitud.REGRESADO_NOVEDAD_INFORME)) && count == numeroRegistros)
                            x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
                        break;
                    default:
                        break;
                }
            } else {
                x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
            }
        }
        return historial;
    }

    @Override
    public List<HistorialPPCompletoDto> buscarHistorialCompleto(long solicitudId) {
        List<Long> ids = this.recuperarSolicitudIdsPadres(solicitudId);
        if (ids.isEmpty())
            return new ArrayList<>();
        Map<String, List<SolicitudHistorial>> historialAgrupado = this.repo.findBySolicitudEnsayo_IdInOrderBySolicitudEnsayoId(ids)
                .stream()
                .collect(Collectors.groupingBy(SolicitudHistorial::getCodigoSolicitud));

        List<HistorialPPCompletoDto> historialFinal = new ArrayList<>();
        historialAgrupado.forEach((key, value) -> {
            List<SolicitudHistorial> historial = value.stream().sorted(Comparator.comparing(SolicitudHistorial::getFechaRegistro)).collect(Collectors.toList());
            historialFinal.add(new HistorialPPCompletoDto(this.mapearHistorialDto(historial), key));
            this.consultarHistorialDDP04(historial.stream().findFirst().get().getSolicitudEnsayo(), historialFinal);
        });

        return historialFinal.stream().sorted(Comparator.comparing(HistorialPPCompletoDto::getFechaInicio)).collect(Collectors.toList());

//        return historialAgrupado.entrySet().stream().map(x -> {
//            List<SolicitudHistorial> historial = x.getValue().stream().sorted(Comparator.comparing(SolicitudHistorial::getFechaRegistro)).collect(Collectors.toList());
//            return new HistorialPPCompletoDto(historial, x.getKey());
//        }).sorted(Comparator.comparing(HistorialPPCompletoDto::getCodigo)).collect(Collectors.toList());
    }

    private List<Long> recuperarSolicitudIdsPadres(long solicitudId) {
        return this.solicitudEnsayoRepo.obtenerSolicitudesHija(solicitudId)
                .stream()
                .map(x -> Long.parseLong(x[0].toString()))
                .collect(Collectors.toList());
    }

    private void consultarHistorialDDP04(SolicitudEnsayo ensayo, List<HistorialPPCompletoDto> historialFinal) {
        if (ensayo.getSolicitudPruebaProcesoId() != null) {
            List<HistorialPPCompletoDto> dto = this.solicitudPruebaProcesoHistorialService.buscarHistorialCompleto(ensayo.getSolicitudPruebaProcesoId());
            dto.forEach(x -> {
                historialFinal.add(new HistorialPPCompletoDto(this.mapearHistorialDDP04Dto(x.getHistorial()), x.getCodigo()));
            });
        }
    }

    private List<SolicitudHistorialDto> mapearHistorialDto(List<SolicitudHistorial> historial) {
        return historial.stream().map(x ->
                new SolicitudHistorialDto(x.getId(), x.getFechaRegistro(), x.getUsuarioNombeCompleto(),
                        x.getSolicitudEnsayo().getId(), x.getSolicitudEnsayo().getCodigo(), x.getOrden().getDescripcion()
                        , x.getObservacion(), false))
                .collect(Collectors.toList());
    }

    private List<SolicitudHistorialDto> mapearHistorialDDP04Dto(List<SolicitudPruebaProcesoHistorial> historial) {
        return historial.stream().map(x ->
                new SolicitudHistorialDto(x.getId(), x.getFechaRegistro(), x.getUsuarioNombeCompleto(),
                        x.getSolicitudPruebasProceso().getId(), x.getSolicitudPruebasProceso().getCodigo(),
                        x.getOrden().getDescripcion(), x.getObservacion(), false))
                .collect(Collectors.toList());
    }
}
