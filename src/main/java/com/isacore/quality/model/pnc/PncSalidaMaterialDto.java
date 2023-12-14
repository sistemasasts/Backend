package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PncSalidaMaterialDto {
    private long id;
    private LocalDate fecha;
    private BigDecimal cantidad;
    private TipoDestino destino;
    private EstadoSalidaMaterial estado;

    //    TODO: Informacion de PNC
    private long idPnc;
    private long idPncDefecto;
    private long numero;
    private String producto;
    private BigDecimal cantidadPnc;
    private BigDecimal saldoPnc;
    private String unidad;
    private String lote;
    private LocalDateTime fechaCreacion;
    private String solicitante;

    private String observacion;

    private String observacionFlujo;
    private PncOrdenFlujo orden;
    private boolean aprobado;
    private LocalDateTime fechaAprobacion;
    private String usuarioAprobador;
    private boolean verPlanesAccion;

    private List<PncPlanAccionDto> planesAccion= new ArrayList();
    public LocalDate getFechaCreacion2() {
        return this.fechaCreacion.toLocalDate();
    }

    public String getEstadoTexto() {
        return getEstado() != null ? getEstado().getDescripcion() : "";
    }

    public String getDestinoDescripcion(){
        return this.destino.getDescripcion();
    }
}
