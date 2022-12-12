package com.isacore.quality.model.spp;

import com.isacore.quality.model.se.SolicitudHistorial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class HistorialPPCompletoDto implements Serializable {
    private String codigo;
    private List<SolicitudPruebaProcesoHistorial> historial = new ArrayList<>();
    private List<SolicitudHistorial> historialSE = new ArrayList<>();

    public HistorialPPCompletoDto(String codigo, List<SolicitudPruebaProcesoHistorial> historial) {
        this.codigo = codigo;
        this.historial = historial;
    }

    public HistorialPPCompletoDto( List<SolicitudHistorial> historialSE, String codigo) {
        this.codigo = codigo;
        this.historialSE = historialSE;
    }
}
