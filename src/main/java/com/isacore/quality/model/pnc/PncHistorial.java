package com.isacore.quality.model.pnc;

import com.isacore.quality.model.se.SolicitudHistorialBase;
import com.isacore.security.model.Usuario;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class PncHistorial extends SolicitudHistorialBase {

    private Long salidaMaterialId;

    private Long pncId;

    @Enumerated(EnumType.STRING)
    private EstadoSalidaMaterial estadoSalidaMaterial;

    @Enumerated(EnumType.STRING)
    private PncOrdenFlujo orden;

    public PncHistorial(String observacion, Usuario usuario, Long salidaMaterialId,
                        Long pncId, EstadoSalidaMaterial estadoSalidaMaterial, PncOrdenFlujo orden) {
        super(observacion, usuario.getNombreUsuario(), usuario.getNombre());
        this.salidaMaterialId = salidaMaterialId;
        this.pncId = pncId;
        this.estadoSalidaMaterial = estadoSalidaMaterial;
        this.orden = orden;
    }
}
