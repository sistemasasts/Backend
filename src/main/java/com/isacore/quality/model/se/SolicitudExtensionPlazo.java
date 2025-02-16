package com.isacore.quality.model.se;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.EntidadBase;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudExtensionPlazo extends EntidadBase {

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaSolicitud;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaInicialSolicitud;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaNueva;

    @Enumerated(EnumType.STRING)
    private EstadoExtensionPlazo estado;

    private String usuarioAprobador;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaAprobacion;

    @Column(columnDefinition = "varchar(max)")
    private String motivo;

    public SolicitudExtensionPlazo(String usuarioAprobador, String motivo, LocalDate fechaInicialSolicitud) {
        this.estado = EstadoExtensionPlazo.PENDIENTE;
        this.usuarioAprobador = usuarioAprobador;
        this.motivo = motivo;
        this.fechaSolicitud = LocalDate.now();
        this.fechaInicialSolicitud = fechaInicialSolicitud;
    }

    public void marcarAprobacion(EstadoExtensionPlazo estado, LocalDate fechaNueva) {
        this.estado = estado;
        this.fechaAprobacion = LocalDate.now();
        this.fechaNueva = fechaNueva;
    }
}
