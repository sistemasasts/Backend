package com.isacore.quality.service.impl.se;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.repository.se.ISolicitudDocumentoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.service.se.ISolicitudHistorialService;

@Service
public class SolicitudHistorialServiceImpl implements ISolicitudHistorialService {

	private static final Log LOG = LogFactory.getLog(SolicitudHistorialServiceImpl.class);
	
	@Autowired
	private ISolicitudHistorialRepo repo;
	
	@Autowired
	private ISolicitudDocumentoRepo repoDocumento;
	
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
		
		return repo.findBySolicitudEnsayo_IdOrderByFechaRegistroAsc(solicitudId).stream().map(x ->{
			x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
			return x;
		}).collect(Collectors.toList());
		
	}

	@Override
	public List<SolicitudHistorial> buscarHistorialPruebasProceso(long solicitudId) {
		return repo.findBySolicitudPruebasProceso_IdOrderByFechaRegistroAsc(solicitudId).stream().map(x ->{
			x.setTieneAdjuntos(repoDocumento.existsByEstadoAndOrdenFlujoAndSolicitudPruebasProceso_Id(x.getEstadoSolicitud(), x.getOrden(), solicitudId));
			return x;
		}).collect(Collectors.toList());
	}

}
