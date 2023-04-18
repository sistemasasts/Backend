package com.isacore.quality.model.bitacora;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bitacora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private String origen;
    private LocalDate fechaLote;
    private String material; // product
    private String lote; // cambiar
    private BigDecimal cantidad; // ver si debe cambiarse
    private String unidad; // ver si se debe cambiar
    private String seguimiento;
    private String afectacion;
    private String motivo;
    private String descripcion;
    private String control;
    private String alcance;
    private String responsable;
    private Integer productId;

    public Bitacora(String origen, LocalDate fechaLote, String material, String lote, BigDecimal cantidad, String unidad, String seguimiento, String afectacion, String motivo, String descripcion, String control, String alcance, String responsable) {
        this.origen = origen;
        this.fechaLote = fechaLote;
        this.material = material;
        this.lote = lote;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.seguimiento = seguimiento;
        this.afectacion = afectacion;
        this.descripcion = descripcion;
        this.motivo = motivo;
        this.control = control;
        this.alcance = alcance;
        this.fechaCreacion = LocalDateTime.now();
        this.responsable = responsable;
    }
}
