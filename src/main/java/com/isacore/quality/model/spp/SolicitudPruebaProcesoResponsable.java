package com.isacore.quality.model.spp;

import com.isacore.util.UtilidadesCadena;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudPruebaProcesoResponsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    private String usuarioResponsable;
    private String usuarioAsignado;

    @Enumerated(EnumType.STRING)
    private OrdenFlujoPP orden;
    @Column(columnDefinition = "bit default 0")
    private boolean activo;
    private LocalDateTime fechaAsignado;
    private LocalDateTime fechaRespuesta;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudPPResponsable estado;

    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudPruebasProceso solicitudPruebasProceso;

    public SolicitudPruebaProcesoResponsable(String usuarioResponsable, OrdenFlujoPP orden, SolicitudPruebasProceso solicitudPruebasProceso) {
        this.usuarioResponsable = usuarioResponsable;
        this.orden = orden;
        this.solicitudPruebasProceso = solicitudPruebasProceso;
        this.activo = true;
        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoSolicitudPPResponsable.PENDIENTE;
    }

    public void asignarUsuario(String usuarioAsignado){
        this.usuarioAsignado = usuarioAsignado;
        this.fechaAsignado = LocalDateTime.now();
    }

    public void marcarComoPendientePorAprobar(){
        this.estado = EstadoSolicitudPPResponsable.POR_APROBAR;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void marcarComoPruebaNoRealizada(){
        this.estado = EstadoSolicitudPPResponsable.PRUEBA_NO_REALIZADA;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void marcarComoPruebaNoRealizadaDefinitiva(){
        this.estado = EstadoSolicitudPPResponsable.PRUEBA_NO_REALIZADA;
        this.fechaRespuesta = LocalDateTime.now();
        this.setActivo(false);
    }

    public void marcarAprobacion(boolean aprobado){
        if(aprobado){
            this.estado = EstadoSolicitudPPResponsable.PROCESADO;
            this.activo = false;
        }else{
            this.estado = EstadoSolicitudPPResponsable.PENDIENTE;
        }
    }

    public boolean necesitaUsuarioAsignado(){
        if(this.estado.equals(EstadoSolicitudPPResponsable.PRUEBA_NO_REALIZADA))
            return UtilidadesCadena.noEsNuloNiBlanco(this.usuarioAsignado);
        return UtilidadesCadena.esNuloOBlanco(this.usuarioAsignado);
    }
}
