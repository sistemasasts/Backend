package com.isacore.quality.service.impl.recordatorio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioPncDetalleDto {
    private String producto;
    private long pncNumero;
    private String cantidad;
    private String fechaLimite;
    private String vigencia;
}
