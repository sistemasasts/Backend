package com.isacore.quality.model.se;

import com.isacore.quality.model.spp.OrdenFlujoPP;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class SolicitudDocumentoDTO implements Serializable {

	private long idSolicitud;
	private OrdenFlujo orden;
	private TipoSolicitud tipoSolicitud;
	private OrdenFlujoPP ordenPP;
}
