package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class VerImagenDTO implements Serializable {
    private byte[] imagen;
    private SolicitudPruebaProcesoDocumento documento;

    public VerImagenDTO(byte[] imagen, SolicitudPruebaProcesoDocumento documento) {
        this.imagen = imagen;
        this.documento = documento;
    }
}
