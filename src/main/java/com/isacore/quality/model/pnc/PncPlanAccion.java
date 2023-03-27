package com.isacore.quality.model.pnc;

import com.isacore.util.UtilidadesCadena;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class PncPlanAccion {

    private static final String EN_TURNO = "x";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaCumplimiento;
    private LocalDateTime fechaVerificacion;
    private LocalDateTime fechaAprobacion;

    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFin;
    @NotNull
    @Column(columnDefinition = "varchar(max)")
    private String descripcion;
    private Boolean cumplido;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private PncSalidaMaterial salidaMaterial;

    @NotNull
    private String responsable;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoPncPlanAccion estado;

    @NotNull
    private int orden;
    private String enTurno;

    public PncPlanAccion(LocalDate fechaInicio, LocalDate fechaFin, String descripcion, PncSalidaMaterial salidaMaterial,
                         String responsable, int orden) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.salidaMaterial = salidaMaterial;
        this.responsable = responsable;
        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoPncPlanAccion.ASIGNADO;
        this.orden = orden;
    }

    public void marcarComoCumplida(boolean cumplimiento) {
        this.cumplido = cumplimiento;
        this.fechaCumplimiento = LocalDateTime.now();
    }

    public void marcarParaValidacion() {
        this.fechaCumplimiento = LocalDateTime.now();
        this.estado = EstadoPncPlanAccion.PENDIENTE_REVISION;
    }

    public void marcarComoRegresado() {
        this.estado = EstadoPncPlanAccion.ASIGNADO;
        this.fechaVerificacion = LocalDateTime.now();
    }

    public void marcarComoAprobado() {
        this.estado = EstadoPncPlanAccion.FINALIZADO;
        this.fechaVerificacion = LocalDateTime.now();
        this.fechaAprobacion = LocalDateTime.now();
        this.cumplido = true;
        this.enTurno = "";
    }

    public void marcarEnTurno() {
        this.enTurno = EN_TURNO;
    }

    public boolean estaEnTurno() {
        return UtilidadesCadena.noEsNuloNiBlanco(this.enTurno) && this.enTurno.equals(EN_TURNO);
    }


    @Override
    public String toString() {
        return "PncPlanAccion{" +
                "id=" + id +
                ", fechaRegistro=" + fechaRegistro +
                ", fechaCumplimiento=" + fechaCumplimiento +
                ", fechaVerificacion=" + fechaVerificacion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", descripcion='" + descripcion + '\'' +
                ", responsable='" + responsable + '\'' +
                ", estado=" + estado +
                ", orden=" + orden +
                '}';
    }
}
