package com.isacore.quality.service.impl.spp;

import com.isacore.quality.model.spp.HistorialPPCompletoDto;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoDocumentoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebasProcesoRepo;
import com.isacore.quality.service.spp.ISolicitudPruebaProcesoHistorialService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudPruebaProcesoHistorialServiceImpl implements ISolicitudPruebaProcesoHistorialService {

	private static final Log LOG = LogFactory.getLog(SolicitudPruebaProcesoHistorialServiceImpl.class);

	private ISolicitudPruebaProcesoHistorialRepo repo;
	private ISolicitudPruebaProcesoDocumentoRepo repoDocumento;
	private ISolicitudPruebasProcesoRepo solicitudPruebasProcesoRepo;

	@Autowired
	public SolicitudPruebaProcesoHistorialServiceImpl(
			ISolicitudPruebaProcesoHistorialRepo repo,
			ISolicitudPruebaProcesoDocumentoRepo repoDocumento,
			ISolicitudPruebasProcesoRepo solicitudPruebasProcesoRepo) {
		this.repo = repo;
		this.repoDocumento = repoDocumento;
		this.solicitudPruebasProcesoRepo = solicitudPruebasProcesoRepo;
	}

	@Override
	public List<SolicitudPruebaProcesoHistorial> findAll() {
		return repo.findAll();
	}

	@Override
	public SolicitudPruebaProcesoHistorial create(SolicitudPruebaProcesoHistorial obj) {
		
		LOG.info(String.format("Historial a guardar %s", obj));
		return repo.save(obj);
	}

	@Override
	public SolicitudPruebaProcesoHistorial findById(SolicitudPruebaProcesoHistorial id) {
		return repo.findById(id.getId()).orElse(null);
	}

	@Override
	public SolicitudPruebaProcesoHistorial update(SolicitudPruebaProcesoHistorial obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SolicitudPruebaProcesoHistorial> buscarHistorial(long solicitudId) {
		
		return repo.findBySolicitudPruebasProceso_IdOrderByFechaRegistroAsc(solicitudId).stream().map(x ->{
			x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudPruebasProceso_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
			return x;
		}).collect(Collectors.toList());
		
	}

	@Override
	public List<HistorialPPCompletoDto> buscarHistorialCompleto(long solicitudId) {
		List<Long> ids = this.recuperarSolicitudIdsPadres(solicitudId);
		if(ids.isEmpty())
			return new ArrayList<>();
		Map<String, List<SolicitudPruebaProcesoHistorial>> historialAgrupado = this.repo.findBySolicitudPruebasProceso_IdInOrderBySolicitudPruebasProcesoId(ids)
				.stream()
				.collect(Collectors.groupingBy(SolicitudPruebaProcesoHistorial::getCodigoSolicitud));
		return historialAgrupado.entrySet().stream().map( x -> {
			List<SolicitudPruebaProcesoHistorial> his = x.getValue().stream().sorted(Comparator.comparing(SolicitudPruebaProcesoHistorial::getFechaRegistro)).collect(Collectors.toList());
			return new HistorialPPCompletoDto(x.getKey(), his);
		}).sorted(Comparator.comparing(HistorialPPCompletoDto::getCodigo)).collect(Collectors.toList());
	}

	private List<Long> recuperarSolicitudIdsPadres(long solicitudId){
		return this.solicitudPruebasProcesoRepo.obtenerSolicitudesHija(solicitudId)
				.stream()
				.map(x -> Long.parseLong(x[0].toString()))
				.collect(Collectors.toList());
	}

}
