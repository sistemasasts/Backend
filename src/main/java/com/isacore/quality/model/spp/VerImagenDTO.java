package com.isacore.quality.model.spp;

import com.isacore.quality.model.se.SolicitudDocumento;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class VerImagenDTO implements Serializable {
    private byte[] imagen;
    private SolicitudPruebaProcesoDocumento documento;
    private SolicitudDocumento documentoEnsayo;

    public VerImagenDTO(byte[] imagen, SolicitudPruebaProcesoDocumento documento) {
        this.imagen = imagen;
        this.documento = documento;
    }

    public VerImagenDTO(byte[] imagen, SolicitudDocumento documentoEnsayo) {
        this.imagen = imagen;
        this.documentoEnsayo = documentoEnsayo;
    }
}
