package com.isacore.quality.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "`ISA_VIEW_TESTS`")
@Subselect("select NEWID() as id, hs.* from ISA_VIEW_TESTS hs")
public class VistaTest implements Serializable {
    @Id
    private String id;
    private String nombrePropiedad;
    private String nombreProducto;
    private String lote;
    @JsonSerialize(using = LocalDateTimeSerializeIsa.class)
    @JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
    private LocalDateTime fecha;
    private String usuario;
    private Double m1Ini;
    private Double m2Ini;
    private Double m3Ini;
    private Double m1End;
    private Double m2End;
    private Double m3End;
    private Double p1;
    private Double p2;
    private Double p3;
    private boolean promediar;
    private Double resultado;
    private String propiedadId;
    private String productoId;
    private String comentario;
    private String proveedor;
    private String res114Batch;
    private String res114Provider;
    private Double speedRpm;
    private Double temperatura;
    private Double tiempoViscocidad;
    private Double torque;
}
