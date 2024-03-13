package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.DocumentoBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class DesviacionRequisitoDocumento extends DocumentoBase {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private DesviacionRequisito desviacionRequisito;

    @Enumerated(EnumType.STRING)
    private DesviacionRequisitoOrdenFlujo orden;

    @Enumerated(EnumType.STRING)
    private EstadoDesviacion estado;

    protected DesviacionRequisitoDocumento() {    }

    public DesviacionRequisitoDocumento(String path, String nombre, DesviacionRequisito desviacionRequisito, DesviacionRequisitoOrdenFlujo orden) {
        super(path, nombre);
        this.desviacionRequisito = desviacionRequisito;
        this.orden = orden;
        this.estado = desviacionRequisito.getEstado();
    }
}
