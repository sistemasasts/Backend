package com.isacore.quality.model.se;

import com.isacore.EntidadBase;
import com.isacore.quality.model.disenoPavimento.Mina;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudEnsayoMina extends EntidadBase {

    @ManyToOne(fetch = FetchType.EAGER)
    private Mina mina;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_ensayo_mina_id", nullable = false)
    private List<SolicitudEnsayoMinaAgregados> agregados = new ArrayList<>();

    public SolicitudEnsayoMina(Mina mina, List<SolicitudEnsayoMinaAgregados> agregados) {
        this.mina = mina;
        this.agregados = agregados;
    }

    public void agregarAgregado(SolicitudEnsayoMinaAgregados agregado) {
        this.agregados.add(agregado);
    }

    @Override
    public String toString() {
        return "SolicitudEnsayoMina{" +
                "mina= id:" + mina.getId() + "nombre:" + mina.getNombre() +
                ", agregados='" + agregados + '\'' +
                '}';
    }
}
