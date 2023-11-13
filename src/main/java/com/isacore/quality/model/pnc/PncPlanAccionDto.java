package com.isacore.quality.model.pnc;

import com.isacore.util.UtilidadesFecha;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PncPlanAccionDto implements Serializable {

    private long id;
    private LocalDateTime fechaCumplimiento;
    private LocalDateTime fechaVerificacion;
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
    private String descripcion;
    private Boolean cumplido;

    private long salidaMaterialId;

    private String responsable;
    private String responsableNombre;

    private EstadoPncPlanAccion estado;
    private int orden;
    private String enTurno;

    private String observacion;
    private PncOrdenFlujo ordenFlujo;
    private boolean aprobado;

//    TODO: informacion pnc
    private long numeroPnc;
    private String nombreProducto;
    private BigDecimal cantidad;
    private String unidad;
    private String destino;
    private String lote;

    public String getEstadoTexto() {
        return this.estado.getDescripcion();
    }

    public String getFechaInicio1(){
        return UtilidadesFecha.formatearLocalDateATexto(fechaInicio, "dd-MM-yyyy");
    }

    public String getFechaFin1(){
        return UtilidadesFecha.formatearLocalDateATexto(fechaFin, "dd-MM-yyyy");
    }

}
