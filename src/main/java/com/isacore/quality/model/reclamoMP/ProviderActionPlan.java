package com.isacore.quality.model.reclamoMP;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.EntidadBase;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "provider_actionplan")
@Table(name = "PROVIDER_ACTIONPLAN")
public class ProviderActionPlan extends EntidadBase {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "PAP_ID")
//    private Integer idPrviderActionPlan;

    @Column(name = "PAP_DESCRIPTION", nullable = true, length = 1024)
    private String description;

    @Column(name = "PAP_DATE_LIMIT", nullable = true)
    private LocalDate dateLimit;

    private LocalDate dateStart;

    @NotNull
    @Column(name = "PAP_RESPONSABLE", nullable = true, length = 1024)
    private String responsable;

    @Enumerated(EnumType.STRING)
    private ComplaintPlanAccionEstado estado;

    protected ProviderActionPlan() {    }

    public ProviderActionPlan(String description, LocalDate dateLimit, LocalDate dateStart, String responsable) {
        this.description = description;
        this.dateLimit = dateLimit;
        this.dateStart = dateStart;
        this.responsable = responsable;
        this.estado = ComplaintPlanAccionEstado.CREADA;
    }

    public void marcarComoEnviadas(){
        this.estado = ComplaintPlanAccionEstado.ASIGNADA;
    }
}
