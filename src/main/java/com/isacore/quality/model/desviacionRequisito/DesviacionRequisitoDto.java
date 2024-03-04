package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.pnc.LineaAfecta;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class DesviacionRequisitoDto {

    private long id;
    private LocalDateTime fechaCreacion;
    private String seguimiento;
    private String motivo;
    private String descripcion;
    private String control;
    private String alcance;
    private String responsable;
    private Long secuencial;

    private String afectacionText;
    private String productTypeText;
    private String productoNombre;


    private DesviacionRequisitoOrdenFlujo orden;
    private String observacion;
    private EstadoDesviacion accion;
}
