package com.isacore.quality.service.impl.se;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.Secuencial;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.se.ISecuencialRepo;

@Service
public class SecuencialServiceImpl {

	@Autowired
	private ISecuencialRepo repo;
	
	public Secuencial ObtenerSecuencialPorTipoSolicitud(TipoSolicitud tipoSolicitud) {

		Optional<Secuencial> secuencialOP = repo.findByTipoSolicitudAndActivoTrue(tipoSolicitud);
		if (!secuencialOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Secuencial para %s no registrado. ", tipoSolicitud));

		Secuencial secuencial = secuencialOP.get();

		secuencial.aumentarSucesion();
		repo.save(secuencial);
		return secuencial;
	}
}
