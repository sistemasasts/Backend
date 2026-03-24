package com.isacore.quality.model.se;

import com.isacore.EntidadBase;
import com.isacore.quality.model.disenoPavimento.Agregado;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudEnsayoMinaAgregados extends EntidadBase {

    @ManyToOne(fetch = FetchType.EAGER)
    private Agregado agregado;

    @Override
    public String toString() {
        return "SolicitudEnsayoMinaAgregados{" +
                "agregado= id:" + agregado.getId() + " nombre: " + agregado.getNombre() +
                '}';
    }
}
