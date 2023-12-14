package com.isacore.quality.model.pnc;

import com.isacore.quality.model.UnidadMedida;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class PncSalidaMaterialInfoAdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoInfoAdd tipoInfoAdd;

    private BigDecimal cantidad;

    private String lote;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidadMedida;

    private String loteOriginal;

    private String loteFin;

    private String usuario;

    private LocalDateTime fechaRegistro;
    @Transient
    private long unidadMedidaId;
    @Transient
    private String unidadMedidaTexto;

    protected PncSalidaMaterialInfoAdd() {    }

    public PncSalidaMaterialInfoAdd(TipoInfoAdd tipoInfoAdd, String usuario) {
        this.tipoInfoAdd = tipoInfoAdd;
        this.usuario = usuario;
    }

    public long getUnidadMedidaId() {
        if(this.unidadMedida != null)
            return this.unidadMedida.getId();
        return unidadMedidaId;
    }

    public String getUnidadMedidaTexto() {
        if(this.unidadMedida != null)
            return this.unidadMedida.getAbreviatura();
        return unidadMedidaTexto;
    }

    public String getTipoInfoAddTexto(){
        return this.tipoInfoAdd.getDescripcion();
    }
}
