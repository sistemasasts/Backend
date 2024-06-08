package com.isacore.quality.model.reclamoMP;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProviderActionPlanDto {

    private long id;
    private long idReclamo;
    private String description;
    private LocalDate dateStart;
    private LocalDate dateLimit;
    private LocalDateTime fechaCierre;
    private String responsable;
    private String responsableNombreCompleto;
    private ComplaintPlanAccionEstado estado;

    private String observacion;

}
