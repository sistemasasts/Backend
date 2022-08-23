package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class SolicitudPruebaProcesoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudPruebasProceso solicitudPruebasProceso;

    private String path;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudPP estado;

    private LocalDateTime fechaSubidaArchivo;

    private String nombreArchivo;

    @Enumerated(EnumType.STRING)
    private OrdenFlujoPP ordenFlujo;

    public SolicitudPruebaProcesoDocumento(SolicitudPruebasProceso solicitudPruebasProceso, String path, String nombreArchivo,
                                           OrdenFlujoPP ordenFlujo) {
        super();
        this.solicitudPruebasProceso = solicitudPruebasProceso;
        this.path = path;
        this.estado = solicitudPruebasProceso.getEstado();
        this.nombreArchivo = nombreArchivo;
        this.ordenFlujo = ordenFlujo;
        this.fechaSubidaArchivo = LocalDateTime.now();
    }

}
