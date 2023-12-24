package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "`VIEW_PNC_COMERCIAL`")
@Subselect("select NEWID() as id_reporte, hs.* from VIEW_PNC_COMERCIAL hs")
public class PncReporteComercialDto implements Serializable {

    @Id
    private String idReporte;
    private long id;
    private long productoId;
    private String nombreProducto;
    private BigDecimal cantidadExistente;
    private String unidad;
    private long numero;
    private String lote;
    private String ubicacion;
    private BigDecimal validez;
    private long defectoId;


}
