package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.pnc.Defecto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class DesviacionRequisitoDefecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defecto_id", nullable = false)
    private Defecto defecto;

    public DesviacionRequisitoDefecto() {   }

    public DesviacionRequisitoDefecto(Defecto defecto) {
        this.defecto = defecto;
    }
}
