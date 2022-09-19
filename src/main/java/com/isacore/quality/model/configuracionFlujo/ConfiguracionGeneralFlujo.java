package com.isacore.quality.model.configuracionFlujo;

import com.isacore.quality.model.se.TipoSolicitud;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class ConfiguracionGeneralFlujo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoSolicitud tipoSolicitud;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NombreConfiguracionFlujo nombreConfiguracionFlujo;
    private String descripcion;
    private String valorConfiguracion;
    private String expresionRegular;

}
