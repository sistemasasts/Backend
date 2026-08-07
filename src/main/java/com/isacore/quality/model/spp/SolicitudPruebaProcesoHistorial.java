package com.isacore.quality.model.spp;

import com.isacore.quality.model.se.SolicitudHistorialBase;
import com.isacore.security.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class SolicitudPruebaProcesoHistorial extends SolicitudHistorialBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudPruebasProceso solicitudPruebasProceso;

	@Enumerated(EnumType.STRING)
	private EstadoSolicitudPP estadoSolicitud;

	@Enumerated(EnumType.STRING)
	private OrdenFlujoPP orden;

	public SolicitudPruebaProcesoHistorial(SolicitudPruebasProceso solicitudE, OrdenFlujoPP orden, Usuario usuario, String observacion) {
		super(observacion, usuario.getNombreUsuario(), usuario.getNombre());
		this.solicitudPruebasProceso = solicitudE;
		this.estadoSolicitud = solicitudE.getEstado();
		this.orden = orden;
	}

	public String getCodigoSolicitud(){
		return this.solicitudPruebasProceso.getCodigo();
	}
	
	
}
