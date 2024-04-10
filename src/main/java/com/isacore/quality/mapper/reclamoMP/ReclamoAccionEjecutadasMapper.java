package com.isacore.quality.mapper.reclamoMP;

import com.isacore.quality.model.reclamoMP.ExecutedAction;
import com.isacore.quality.model.reclamoMP.ExecutedActionDto;
import com.isacore.quality.model.reclamoMP.ProviderActionPlan;
import com.isacore.quality.model.reclamoMP.ProviderActionPlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReclamoAccionEjecutadasMapper {

    @Mapping(target = "idReclamo", ignore = true)
    ExecutedActionDto fromExecutedActionToDto(ExecutedAction complaint);

    default List<ExecutedActionDto> fromListToListDto(List<ExecutedAction> honorarios) {
        return honorarios.stream().map(this::fromExecutedActionToDto).collect(Collectors.toList());
    }

}
