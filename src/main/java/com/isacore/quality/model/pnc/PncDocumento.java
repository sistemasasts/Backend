package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PncDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private long productoNoConformeId;
    private Long salidaMaterialId;
    private Long pncDefectoId;
    private Long pncPlanAccionId;

    private String path;
    @Enumerated(EnumType.STRING)
    private EstadoSalidaMaterial estado;
    private LocalDateTime fechaSubidaArchivo;
    private String nombreArchivo;
    @Enumerated(EnumType.STRING)
    private PncOrdenFlujo ordenFlujo;
    @Enumerated(EnumType.STRING)
    private EstadoPncPlanAccion estadoPlanAccion;

    private String tipo;

    @Transient
    private String base64;

    public PncDocumento(long productoNoConformeId, Long salidaMaterialId, Long pncDefectoId, Long pncPlanAccionId, String path,
                        EstadoSalidaMaterial estado, String nombreArchivo, PncOrdenFlujo ordenFlujo, String tipo, EstadoPncPlanAccion estadoPlanAccion) {
        this.productoNoConformeId = productoNoConformeId;
        this.salidaMaterialId = salidaMaterialId;
        this.pncDefectoId = pncDefectoId;
        this.pncPlanAccionId = pncPlanAccionId;
        this.path = path;
        this.estado = estado;
        this.nombreArchivo = nombreArchivo;
        this.ordenFlujo = ordenFlujo;
        this.tipo = tipo;
        this.estadoPlanAccion = estadoPlanAccion;
        this.fechaSubidaArchivo = LocalDateTime.now();
    }
}
