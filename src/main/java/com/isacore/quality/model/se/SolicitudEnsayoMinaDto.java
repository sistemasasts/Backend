package com.isacore.quality.model.se;

import com.isacore.quality.model.disenoPavimento.MinaDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SolicitudEnsayoMinaDto {
    private long id;
    private MinaDto mina;
    private List<SolicitudEnsayoMinaAgregadosDto> agregados;
}
