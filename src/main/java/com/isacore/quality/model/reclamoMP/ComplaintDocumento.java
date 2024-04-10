package com.isacore.quality.model.reclamoMP;

import com.isacore.quality.model.DocumentoBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
public class ComplaintDocumento extends DocumentoBase {

    private long solicitudId;
    private String estado;
    private String orden;
    @Enumerated(EnumType.STRING)
    private OrigenSolicitud origen;

    protected ComplaintDocumento() { }

    public ComplaintDocumento(String path, String nombreArchivo, long solicitudId, String estado, String orden,
                              OrigenSolicitud origen) {
        super(path, nombreArchivo);
        this.solicitudId = solicitudId;
        this.estado = estado;
        this.orden = orden;
        this.origen = origen;
    }
}
