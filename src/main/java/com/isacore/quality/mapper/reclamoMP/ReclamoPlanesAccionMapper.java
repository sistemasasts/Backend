package com.isacore.quality.mapper.reclamoMP;

import com.isacore.quality.model.reclamoMP.ProviderActionPlan;
import com.isacore.quality.model.reclamoMP.ProviderActionPlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReclamoPlanesAccionMapper {
    @Mapping(target = "idReclamo", ignore = true)
    ProviderActionPlanDto fromProviderActionPlanToDto(ProviderActionPlan providerActionPlan);

    default List<ProviderActionPlanDto> fromListToListDto(List<ProviderActionPlan> honorarios) {
        return honorarios.stream().map(this::fromProviderActionPlanToDto).collect(Collectors.toList());
    }
}
