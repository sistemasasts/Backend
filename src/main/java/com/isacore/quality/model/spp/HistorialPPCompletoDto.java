package com.isacore.quality.model.spp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class HistorialPPCompletoDto implements Serializable {
    private String codigo;
    private List<SolicitudPruebaProcesoHistorial> historial;
}
