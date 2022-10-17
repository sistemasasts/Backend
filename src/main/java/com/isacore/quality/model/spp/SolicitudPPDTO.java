package com.isacore.quality.model.spp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class SolicitudPPDTO implements Serializable {

    private Long id;
    private String codigo;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaCreacion;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaAprobacion;

    private String nombreSolicitante;
    private String usuarioGestion;
    private String usuarioAprobador;
    private EstadoSolicitudPP estado;
    private String proveedorNombre;
    private Integer proveedorId;
    private String lineaAplicacion;
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntrega;
    private String detalleMaterial;
    private TipoAprobacionPP tipoAprobacion;
    private boolean aprobado;
    private Area area;
    private OrigenSolicitudPP origen;
    private boolean requiereInforme;
    private String usuarioValidador;
    private String usuarioGestionPlanta;
    private String usuarioGestionCalidadJefe;
    private String usuarioGestionCalidad;
    private String usuarioGestionMantenimientoJefe;
    private String usuarioGestionMantenimiento;
    private LocalDate fechaPrueba;
    private LocalDate fechaEntregaInforme;

    public LocalDate getFechaCreacion2() {
        return this.fechaCreacion.toLocalDate();
    }

    public String getTipoAprobacionTexto() {
        return this.tipoAprobacion != null ? this.tipoAprobacion.getDescripcion() : "";
    }

    public String getNombreArea() {
        return this.area != null ? this.area.getNameArea() : "";
    }
    public String getAprobadoTexto(){
        return getTipoAprobacion() != null ? isAprobado() ? "SI" : "NO" : "";
    }

    public String getRequiereInformeTexto(){
        return isRequiereInforme() ?  "SI" : "NO";
    }

    public String getOrigenTexto(){
        return getOrigen() != null ? this.origen.getDescripcion() : "";
    }
}
