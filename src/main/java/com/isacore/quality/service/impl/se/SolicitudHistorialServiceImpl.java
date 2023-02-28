package com.isacore.quality.service.impl.se;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.model.spp.HistorialPPCompletoDto;
import com.isacore.quality.repository.se.ISolicitudDocumentoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.service.se.ISolicitudHistorialService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public SolicitudHistorialServiceImpl(ISolicitudHistorialRepo repo, ISolicitudDocumentoRepo repoDocumento, ISolicitudEnsayoRepo solicitudEnsayoRepo) {
        this.repo = repo;
        this.repoDocumento = repoDocumento;
        this.solicitudEnsayoRepo = solicitudEnsayoRepo;
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
        return historialAgrupado.entrySet().stream().map(x -> {
            List<SolicitudHistorial> historial = x.getValue().stream().sorted(Comparator.comparing(SolicitudHistorial::getFechaRegistro)).collect(Collectors.toList());
            return new HistorialPPCompletoDto(historial, x.getKey());
        }).sorted(Comparator.comparing(HistorialPPCompletoDto::getCodigo)).collect(Collectors.toList());
    }

    private List<Long> recuperarSolicitudIdsPadres(long solicitudId) {
        return this.solicitudEnsayoRepo.obtenerSolicitudesHija(solicitudId)
                .stream()
                .map(x -> Long.parseLong(x[0].toString()))
                .collect(Collectors.toList());
    }

//	@Override
//	public List<SolicitudHistorial> buscarHistorialPruebasProceso(long solicitudId) {
//		return repo.findBySolicitudPruebasProceso_IdOrderByFechaRegistroAsc(solicitudId).stream().map(x ->{
//			x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudPruebasProceso_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
//			return x;
//		}).collect(Collectors.toList());
//	}

}
