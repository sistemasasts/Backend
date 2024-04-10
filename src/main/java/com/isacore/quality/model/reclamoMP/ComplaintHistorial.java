package com.isacore.quality.model.reclamoMP;

import com.isacore.quality.model.se.SolicitudHistorialBase;
import com.isacore.sgc.acta.model.UserImptek;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity
public class ComplaintHistorial extends SolicitudHistorialBase {

    @Enumerated(EnumType.STRING)
    private ComplaintOrdenFlujo orden;

    private String estado;

    private long solicitudId;

    protected ComplaintHistorial (){}

    public ComplaintHistorial(String observacion, UserImptek usuario, ComplaintOrdenFlujo orden, String estado, long solicitudId) {
        super(observacion, usuario.getIdUser(), usuario.getEmployee().getCompleteName());
        this.orden = orden;
        this.estado = estado;
        this.solicitudId = solicitudId;
    }
}
