package com.isacore.quality.model.se;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class SolicitudHistorialBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaRegistro;

    @Column(columnDefinition = "varchar(max)")
    private String observacion;

    private String usuarioId;

    private String usuarioNombeCompleto;

    private Boolean mostrar;

    @Transient
    private Boolean tieneAdjuntos;

    protected SolicitudHistorialBase(){}

    public SolicitudHistorialBase(String observacion, String usuarioId, String usuarioNombeCompleto) {
        this.observacion = observacion;
        this.usuarioId = usuarioId;
        this.usuarioNombeCompleto = usuarioNombeCompleto;
        this.mostrar = true;
        this.fechaRegistro = LocalDateTime.now();
    }
}
