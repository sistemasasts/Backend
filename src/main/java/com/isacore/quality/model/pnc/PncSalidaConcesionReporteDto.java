package com.isacore.quality.model.pnc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PncSalidaConcesionReporteDto implements Serializable {
    private String producto;
    private String responsableVenta;
    private String responsableBodega;
    private String cliente;
    private LocalDate fechaSalida;
    private BigDecimal cantidad;
    private BigDecimal validez;
    private String lote;
    private String factura;
    private String codigoPnc;
    private String detalleNoConformidad;
    private String unidad;

    public PncSalidaConcesionReporteDto(String producto, String responsableVenta, String responsableBodega,
                                        String cliente, LocalDate fechaSalida, BigDecimal cantidad, BigDecimal validez,
                                        String lote, String factura, String codigoPnc, String detalleNoConformidad,
                                        String unidad) {
        this.producto = producto;
        this.responsableVenta = responsableVenta;
        this.responsableBodega = responsableBodega;
        this.cliente = cliente;
        this.fechaSalida = fechaSalida;
        this.cantidad = cantidad;
        this.validez = validez;
        this.lote = lote;
        this.factura = factura;
        this.codigoPnc = codigoPnc;
        this.detalleNoConformidad = detalleNoConformidad;
        this.unidad = unidad;
    }
}
