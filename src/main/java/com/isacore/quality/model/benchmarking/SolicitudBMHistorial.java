package com.isacore.quality.model.benchmarking;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.SolicitudHistorialBase;
import com.isacore.sgc.acta.model.UserImptek;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudBMHistorial extends SolicitudHistorialBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudBM solicitudBM;

	@Enumerated(EnumType.STRING)
	private EstadoSolicitud estadoSolicitud;

	@Enumerated(EnumType.STRING)
	private OrdenFlujo orden;

	public SolicitudBMHistorial(SolicitudBM solicitudBM, OrdenFlujo orden, UserImptek usuario, String observacion) {
		super(observacion, usuario.getIdUser(), usuario.getEmployee().getCompleteName());
		this.solicitudBM = solicitudBM;
		this.estadoSolicitud = solicitudBM.getEstado();
		this.orden = orden;
	}
    public String getCodigoSolicitud(){
        return this.solicitudBM.getCodigo();
    }
}
