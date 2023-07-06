package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DesviacionRequisitoReporteDTO implements Serializable {
    private String nombreProducto;
    private String afectacion;
    private String motivo;
    private String descripcion;
    private ProductType tipoProducto;
    private Long secuencial;
    private String control;
    private String alcance;
    private List<Lote> listaLotes;
    private String seguimiento;

    public DesviacionRequisitoReporteDTO(
        DesviacionRequisito desviacionRequisito,
        List<Lote> lotes
    ) {
        this.nombreProducto = desviacionRequisito.getProduct().getNameProduct();
        this.afectacion = desviacionRequisito.getAfectacion().getDescripcion();
        this.seguimiento = desviacionRequisito.getSeguimiento();
        this.descripcion = desviacionRequisito.getDescripcion();
        this.control = desviacionRequisito.getControl();
        this.motivo = desviacionRequisito.getMotivo();
        this.tipoProducto = desviacionRequisito.getProduct().getTypeProduct();
        this.alcance = desviacionRequisito.getAlcance();
        this.listaLotes = lotes;
        this.secuencial = desviacionRequisito.getSecuencial();
    }

    public String getTipoProductoTexto(){
        return  this.tipoProducto.getDescripcion();
    }
}
