package com.isacore.quality.model.solicitudPlanAccion;

import com.isacore.quality.model.se.TipoSolicitud;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudPlanAccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFin;
    @NotNull
    @Column(columnDefinition = "varchar(max)")
    private String descripcion;
    private Boolean cumplido;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;
    @NotNull
    private long solicitudId;

    public SolicitudPlanAccion(LocalDate fechaInicio,
                               LocalDate fechaFin,
                               String descripcion,
                               Boolean cumplido,
                               TipoSolicitud tipoSolicitud,
                               long solicitudId) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.cumplido = cumplido;
        this.tipoSolicitud = tipoSolicitud;
        this.solicitudId = solicitudId;
        this.fechaRegistro = LocalDateTime.now();
    }
}
