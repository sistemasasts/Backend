package com.isacore.quality.service.impl.recordatorio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioDetalleDto {
    private String codigo;
    private String tipoSolicitud;
    private String fechaLimite;
    private String vigencia;
}
