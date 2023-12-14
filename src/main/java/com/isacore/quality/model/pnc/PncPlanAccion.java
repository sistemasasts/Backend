package com.isacore.quality.model.pnc;

import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesFecha;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
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

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean llenarInfoAdicional;

    public PncPlanAccion(LocalDate fechaInicio, LocalDate fechaFin, String descripcion, PncSalidaMaterial salidaMaterial,
                         String responsable, int orden, boolean llenarInfoAdicional) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.salidaMaterial = salidaMaterial;
        this.responsable = responsable;
        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoPncPlanAccion.ASIGNADO;
        this.orden = orden;
        this.llenarInfoAdicional = llenarInfoAdicional;
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

    public String getFechaInicio1(){
        return UtilidadesFecha.formatearLocalDateATexto(fechaInicio, "dd-MM-yyyy");
    }

    public String getFechaFin1(){
        return UtilidadesFecha.formatearLocalDateATexto(fechaFin, "dd-MM-yyyy");
    }

    public int getVigencia(){
        Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaFin.atStartOfDay());
        return (int) diff.toDays();
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
