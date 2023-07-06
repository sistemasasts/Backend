package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PncDefectoDto {
    private Long id;
    private String defecto;
    private String unidad;
    private String ubicacion;
    private BigDecimal validez;
    private long idImagen;
    private BigDecimal cantidad;
    private String tipoImagen;
    private String base64;
}
