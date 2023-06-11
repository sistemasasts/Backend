package com.isacore.quality.model.pnc;

import com.isacore.quality.model.UnidadMedida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PncDefecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Defecto defecto;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;

    @NotNull
    private String ubicacion;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal validez;

    private long idImagen;

    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal cantidad;

    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal saldo;

    @Transient
    private long productoNoConformeId;

    @Transient
    private boolean nuevo;

    public String getDefectoDescripcion(){
        return this.defecto.getNombre();
    }

    public String getUnidadDescripcion(){
        return this.unidad.getAbreviatura();
    }

    public void reducirStock(BigDecimal cantidad) {
        this.setSaldo(this.saldo.subtract(cantidad));
    }

    public String getDescripcionCompleta(){
        return String.format("%s | %s | %s", getDefectoDescripcion(), getUbicacion(), getSaldo());
    }

}
