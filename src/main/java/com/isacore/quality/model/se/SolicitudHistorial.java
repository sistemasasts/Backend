package com.isacore.quality.model.se;

import com.isacore.sgc.acta.model.UserImptek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudHistorial extends SolicitudHistorialBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudEnsayo solicitudEnsayo;

	@Enumerated(EnumType.STRING)
	private EstadoSolicitud estadoSolicitud;

	@Enumerated(EnumType.STRING)
	private OrdenFlujo orden;

	public SolicitudHistorial(SolicitudEnsayo solicitudEnsayo, OrdenFlujo orden, UserImptek usuario, String observacion) {
		super(observacion, usuario.getIdUser(), usuario.getEmployee().getCompleteName());
		this.solicitudEnsayo = solicitudEnsayo;
		this.estadoSolicitud = solicitudEnsayo.getEstado();
		this.orden = orden;
	}
    public String getCodigoSolicitud(){
        return this.solicitudEnsayo.getCodigo();
    }
}
