package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.se.SolicitudHistorialBase;
import com.isacore.sgc.acta.model.UserImptek;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class DesviacionRequisitoHistorial extends SolicitudHistorialBase {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private DesviacionRequisito desviacionRequisito;

    @Enumerated(EnumType.STRING)
    private DesviacionRequisitoOrdenFlujo orden;

    @Enumerated(EnumType.STRING)
    private EstadoDesviacion estado;

    protected DesviacionRequisitoHistorial(){}

    public DesviacionRequisitoHistorial(String observacion, UserImptek usuario,
                                        DesviacionRequisito desviacionRequisito, DesviacionRequisitoOrdenFlujo orden,
                                        EstadoDesviacion estado) {
        super(observacion, usuario.getIdUser(), usuario.getEmployee().getCompleteName());
        this.desviacionRequisito = desviacionRequisito;
        this.orden = orden;
        this.estado = estado;
    }
}
