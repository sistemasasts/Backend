package com.isacore.quality.model.se;

import com.isacore.security.model.Usuario;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudHistorial extends SolicitudHistorialBase {

    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudEnsayo solicitudEnsayo;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoSolicitud;

    @Enumerated(EnumType.STRING)
    private OrdenFlujo orden;

    public SolicitudHistorial(SolicitudEnsayo solicitudEnsayo, OrdenFlujo orden, Usuario usuario, String observacion) {
        super(observacion, usuario.getNombreUsuario(), usuario.getNombre());
        this.solicitudEnsayo = solicitudEnsayo;
        this.estadoSolicitud = solicitudEnsayo.getEstado();
        this.orden = orden;
    }

    public String getCodigoSolicitud() {
        return this.solicitudEnsayo.getCodigo();
    }
}
