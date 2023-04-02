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

    @Transient
    private long productoNoConformeId;

    @Transient
    private boolean nuevo;
}
