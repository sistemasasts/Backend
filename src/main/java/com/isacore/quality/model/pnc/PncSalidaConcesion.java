package com.isacore.quality.model.pnc;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class PncSalidaConcesion extends EntidadBase {

    private String cliente;
    private String factura;
    private String responsableVenta;
    private String responsableBodega;

    protected PncSalidaConcesion(){ }

    public PncSalidaConcesion(String cliente, String factura, String responsableVenta, String bodega) {
        this.cliente = cliente;
        this.factura = factura;
        this.responsableVenta = responsableVenta;
        this.responsableBodega = bodega;
    }

    @Override
    public String toString() {
        return "{"+
                " Cliente:" + this.cliente +
                " factura:" + this.factura +
                " bodega:" + this.responsableBodega +
                " responsable venta:" + this.responsableVenta +
                "}";
    }
}
