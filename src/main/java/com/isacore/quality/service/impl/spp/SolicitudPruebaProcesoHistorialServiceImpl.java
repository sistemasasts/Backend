package com.isacore.quality.service.impl.spp;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoDocumentoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.service.spp.ISolicitudPruebaProcesoHistorialService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudPruebaProcesoHistorialServiceImpl implements ISolicitudPruebaProcesoHistorialService {

	private static final Log LOG = LogFactory.getLog(SolicitudPruebaProcesoHistorialServiceImpl.class);
	
	@Autowired
	private ISolicitudPruebaProcesoHistorialRepo repo;
	
	@Autowired
	private ISolicitudPruebaProcesoDocumentoRepo repoDocumento;
	
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

}
