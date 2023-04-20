package com.isacore.quality.model.spp;

import com.isacore.quality.model.se.SolicitudHistorialDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class HistorialPPCompletoDto implements Serializable {
    private String codigo;
    private LocalDateTime fechaInicio;
    private List<SolicitudPruebaProcesoHistorial> historial = new ArrayList<>();
    private List<SolicitudHistorialDto> historialSE = new ArrayList<>();

    public HistorialPPCompletoDto(String codigo, List<SolicitudPruebaProcesoHistorial> historial) {
        this.codigo = codigo;
        this.historial = historial;
    }

    public HistorialPPCompletoDto( List<SolicitudHistorialDto> historialSE, String codigo) {
        this.codigo = codigo;
        this.historialSE = historialSE;
        this.fechaInicio = historialSE.stream().findFirst().get().getFechaRegistro();
    }
}
