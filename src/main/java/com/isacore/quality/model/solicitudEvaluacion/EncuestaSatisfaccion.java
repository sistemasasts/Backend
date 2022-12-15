package com.isacore.quality.model.solicitudEvaluacion;

import com.isacore.quality.model.se.TipoSolicitud;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class EncuestaSatisfaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;
    private String usuario;

    private long solicitudId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EscalaSatisfaccion satisfaccion;

    @Column(columnDefinition = "varchar(max)")
    private String comentario;

    public EncuestaSatisfaccion(long solicitudId, TipoSolicitud tipoSolicitud, EscalaSatisfaccion satisfaccion, String comentario, String usuario) {
        this.solicitudId = solicitudId;
        this.tipoSolicitud = tipoSolicitud;
        this.satisfaccion = satisfaccion;
        this.comentario = comentario;
        this.usuario = usuario;
        this.fechaRegistro = LocalDateTime.now();
    }
}
