package com.isacore.quality.model.se;

import com.isacore.EntidadBase;
import com.isacore.quality.model.disenoPavimento.TipoDiseno;
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
public class SolicitudEnsayoDisenio extends EntidadBase {

    @ManyToOne(fetch = FetchType.EAGER)
    private TipoDiseno tipoDiseno;

    public SolicitudEnsayoDisenio(TipoDiseno tipoDiseno) {
        this.tipoDiseno = tipoDiseno;
    }
}
