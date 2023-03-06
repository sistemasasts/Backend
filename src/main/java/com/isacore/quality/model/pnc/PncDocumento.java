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

    private String path;
    @Enumerated(EnumType.STRING)
    private EstadoSalidaMaterial estado;
    private LocalDateTime fechaSubidaArchivo;
    private String nombreArchivo;
    @Enumerated(EnumType.STRING)
    private PncOrdenFlujo ordenFlujo;
    private String tipo;

    public PncDocumento(long productoNoConformeId, Long salidaMaterialId, Long pncDefectoId, String path,
                        EstadoSalidaMaterial estado, String nombreArchivo, PncOrdenFlujo ordenFlujo, String tipo) {
        this.productoNoConformeId = productoNoConformeId;
        this.salidaMaterialId = salidaMaterialId;
        this.pncDefectoId = pncDefectoId;
        this.path = path;
        this.estado = estado;
        this.nombreArchivo = nombreArchivo;
        this.ordenFlujo = ordenFlujo;
        this.tipo = tipo;
        this.fechaSubidaArchivo = LocalDateTime.now();
    }
}
