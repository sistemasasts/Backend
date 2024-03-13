package com.isacore.quality.model.comunes;

import com.isacore.EntidadBase;
import com.isacore.quality.model.se.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
public class MatrizAprobacionAdicional extends EntidadBase {
    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;

    private String usuario;

    private int orden;

    @Enumerated(EnumType.STRING)
    private TipoAprobacion tipoAprobacion;

    protected MatrizAprobacionAdicional() {}
}
