package com.isacore.quality.mapper.reclamoMP;

import com.isacore.quality.model.reclamoMP.ProviderActionPlan;
import com.isacore.quality.model.reclamoMP.ProviderActionPlanDto;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.StaticInjector;
import com.isacore.util.UtilidadesCadena;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReclamoPlanesAccionMapper {
    @Mapping(target = "idReclamo", ignore = true)
    @Mapping(target = "observacion", ignore = true)
    @Mapping(target = "responsableNombreCompleto", source = "responsable", qualifiedByName = "responsableNombre")
    ProviderActionPlanDto fromProviderActionPlanToDto(ProviderActionPlan providerActionPlan);

    default List<ProviderActionPlanDto> fromListToListDto(List<ProviderActionPlan> honorarios) {
        return honorarios.stream().map(this::fromProviderActionPlanToDto).collect(Collectors.toList());
    }

    @Named("responsableNombre")
    default String responsableNombre(String usuario) {
        if(UtilidadesCadena.esNuloOBlanco(usuario))
            return "";
        final IUserImptekRepo repo = StaticInjector.getInstance().getBean(IUserImptekRepo.class);
        UserImptek usuarioRecuperado = repo.findByIdUser(usuario).orElse(null);
        return usuarioRecuperado == null ? "": usuarioRecuperado.getEmployee().getCompleteName();
    }
}
