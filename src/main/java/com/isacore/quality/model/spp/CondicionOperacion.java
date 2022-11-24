package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class CondicionOperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;
    private String proceso;
    private String observacion;

    @Enumerated(EnumType.STRING)
    private CondicionOperacionTipo tipo;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "condicion_operacion_id")
    private List<Condicion> condiciones = new ArrayList<>();

    public CondicionOperacion(String proceso, String observacion, CondicionOperacionTipo tipo) {
        this.proceso = proceso;
        this.observacion = observacion;
        this.tipo = tipo;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void agregarCondicion(Condicion condicion){
        this.condiciones.add(condicion);
    }

    public void eliminarCondicion(long condicionId){
        this.condiciones.removeIf(x -> x.getId() == condicionId);
    }
}
