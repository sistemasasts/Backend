package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class MinaAgregadoQuimica extends EntidadBase {

    @ManyToOne(fetch = FetchType.EAGER)
    private AgregadoQuimica agregadoQuimica;
}
