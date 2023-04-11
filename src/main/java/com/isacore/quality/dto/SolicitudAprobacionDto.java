package com.isacore.quality.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SolicitudAprobacionDto implements Serializable {
    private long id;
    private String codigo;
    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaCreacion;
    private String proveedor;
    private String estado;
    private String nombreSolicitante;
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntrega;
    private String detalleMaterial;
    private TipoSolicitud tipo;

    private LocalDate fechaEntregaInforme;
    private int vigencia;
    private String lineaAplicacion;
    private String motivo;
    private String observacion;

    public SolicitudAprobacionDto(long id, String codigo, LocalDateTime fechaCreacion, String proveedor, String estado, String nombreSolicitante,
                                  LocalDate fechaEntrega, String detalleMaterial,LocalDate fechaEntregaInforme, int vigencia,TipoSolicitud tipo,
                                  String observacion) {
        this.id = id;
        this.codigo = codigo;
        this.fechaCreacion = fechaCreacion;
        this.proveedor = proveedor;
        this.estado = estado;
        this.nombreSolicitante = nombreSolicitante;
        this.fechaEntrega = fechaEntrega;
        this.detalleMaterial = detalleMaterial;
        this.fechaEntregaInforme = fechaEntregaInforme;
        this.vigencia = vigencia;
        this.tipo = tipo;
        this.observacion = observacion;
    }

    public String getFechaSolicitud() {
        return UtilidadesFecha.formatear(getFechaCreacion(), "YYYY-MM-dd");
    }
    public String getTipoTexto(){
        return this.tipo.getDescripcion();
    }
}
