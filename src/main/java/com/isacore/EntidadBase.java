package com.isacore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@FieldNameConstants(onlyExplicitlyIncluded = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public abstract class EntidadBase extends EntidadBaseId {
    public static final String PROPIEDAD_FECHA_CREACION = "fechaCreacion";
    public static final String PROPIEDAD_FECHA_MODIFICACION = "fechaModificacion";

    @FieldNameConstants.Include
    @CreatedDate
    @JsonIgnore
    private LocalDateTime creadoFecha;

    @CreatedBy
    @NotNull
    @JsonIgnore
    private String creadoPor;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime modificadoFecha;

    @LastModifiedBy
    @NotNull
    @JsonIgnore
    private String modificadoPor;

    public LocalDateTime getCreadoFecha() {
        return creadoFecha;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public LocalDateTime getModificadoFecha() {
        return modificadoFecha;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setCreadoFecha(LocalDateTime creadoFecha) {
        this.creadoFecha = creadoFecha;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
}
