package com.isacore.quality.model.pnc;

import com.isacore.quality.model.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PncReporteDto implements Serializable {
    private String nombreProducto;
    private BigDecimal cantidad;
    private String unidad;
    private long numero;
    private LocalDate fechaProduccion;
    private LocalDate fechaDeteccion;
    private String lote;
    private String entradaTraspaso;
    private String areaInvolucrada;
    private BigDecimal pesoTotalNC;
    private String observaciones5m;
    private String elaboradoPor;
    private String ordenProduccion;
    private String procedenciaProducto;
    private ProductType tipoProducto;
    private List<PncDefecto> defectos;
    private List<PncSalidaMaterialDto> listaSalidaMaterial;

    public PncReporteDto(ProductoNoConforme pnc, String nombreResponsable, List<PncSalidaMaterialDto> salidaMaterials) {
        this.nombreProducto = pnc.getProducto().getNameProduct();
        this.cantidad = pnc.getCantidadNoConforme();
        this.unidad = pnc.getUnidad().getAbreviatura();
        this.numero = pnc.getNumero();
        this.fechaProduccion = pnc.getFechaProduccion().toLocalDate();
        this.fechaDeteccion = pnc.getFechaDeteccion();
        this.lote = pnc.getLote();
        this.entradaTraspaso = pnc.getHcc();
        this.areaInvolucrada = pnc.getArea().getNameArea();
        this.pesoTotalNC = pnc.getPesoNoConforme();
        this.observaciones5m = pnc.getObservacionCincoMs();
        this.ordenProduccion = pnc.getOrdenProduccion();
        this.tipoProducto = pnc.getProducto().getTypeProduct();
        this.defectos = pnc.getDefectos();
        this.elaboradoPor = nombreResponsable;
        this.listaSalidaMaterial = salidaMaterials;
    }

    public String getProcedenciaProducto() {
        if (tipoProducto.equals(ProductType.PRODUCTO_TERMINADO))
            return "x";
        else
            return "-";
    }

    public String getProcedenciaProductoPP() {
        if (tipoProducto.equals(ProductType.PRODUCTO_EN_PROCESO))
            return "x";
        else
            return "-";
    }
}
