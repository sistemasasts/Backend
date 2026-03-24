package com.isacore.quality.model.se;

import com.isacore.quality.model.disenoPavimento.AgregadoDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class SolicitudEnsayoMinaAgregadosDto {
    private long id;
    private AgregadoDto agregado;
}
