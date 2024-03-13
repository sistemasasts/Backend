package com.isacore.quality.model;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class DocumentoBase extends EntidadBase {
    private String path;

    private LocalDateTime fechaSubidaArchivo;

    private String nombreArchivo;

    protected DocumentoBase() {}

    public DocumentoBase(String path, String nombreArchivo) {
        this.path = path;
        this.nombreArchivo = nombreArchivo;
        this.fechaSubidaArchivo = LocalDateTime.now();
    }
}
