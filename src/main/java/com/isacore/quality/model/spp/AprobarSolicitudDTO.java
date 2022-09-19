package com.isacore.quality.model.spp;

import lombok.Data;

import java.io.Serializable;

@Data
public class AprobarSolicitudDTO implements Serializable {
    private long solicitudId;
    private boolean aprobar;
    private String observacion;
    private OrdenFlujoPP orden;
    private TipoAprobacionPP tipoAprobacion;
}
