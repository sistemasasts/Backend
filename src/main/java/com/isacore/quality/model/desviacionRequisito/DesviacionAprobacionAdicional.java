package com.isacore.quality.model.desviacionRequisito;

import com.isacore.EntidadBase;
import com.isacore.quality.model.comunes.TipoAprobacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class DesviacionAprobacionAdicional extends EntidadBase {

    private LocalDateTime fechaAprobacion;

    private String usuario;

    private int orden;

    private boolean activo;

    @Enumerated(EnumType.STRING)
    private TipoAprobacion tipoAprobacion;

    public DesviacionAprobacionAdicional() {}

    public DesviacionAprobacionAdicional(String usuario, int orden, TipoAprobacion tipoAprobacion) {
        this.usuario = usuario;
        this.orden = orden;
        this.tipoAprobacion = tipoAprobacion;
    }
}
