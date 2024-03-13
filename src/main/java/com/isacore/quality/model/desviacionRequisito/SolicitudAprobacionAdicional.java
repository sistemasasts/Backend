package com.isacore.quality.model.desviacionRequisito;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class SolicitudAprobacionAdicional extends EntidadBase {

    @Enumerated(EnumType.STRING)
    private SolicitudAprobacionAdicionalEstado estado;

    private LocalDateTime fechaExpiracion;

    private LocalDateTime fechaSolicitud;

    @NotNull
    private String prenda;

    @ManyToOne(fetch = FetchType.EAGER)
    private DesviacionAprobacionAdicional desviacionAprobacionAdicional;

    public SolicitudAprobacionAdicional() {    }

    public SolicitudAprobacionAdicional(String prenda, DesviacionAprobacionAdicional desviacionAprobacionAdicional) {
        this.estado = SolicitudAprobacionAdicionalEstado.SOLICITADO;
        this.prenda = prenda;
        this.fechaSolicitud = LocalDateTime.now();
        this.desviacionAprobacionAdicional = desviacionAprobacionAdicional;
    }
}
