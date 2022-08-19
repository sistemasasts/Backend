package com.isacore.quality.model.se;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class SolicitudDocumentoDTO implements Serializable {

	private long idSolicitud;
	private OrdenFlujo orden;
	private TipoSolicitud tipoSolicitud;
	
}
