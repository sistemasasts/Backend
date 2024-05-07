package com.isacore.quality.mapper.reclamoMP;

import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintDto;
import com.isacore.quality.model.reclamoMP.Problem;
import com.isacore.quality.model.reclamoMP.ProblemDto;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {Problem.class})
public interface ProblemaMapper {

//    @Mapping(target = "creado_fecha", ignore = true)
//    @Mapping(target = "creado_por", ignore = true)
//    @Mapping(target = "modificado_fecha", ignore = true)
    @Mapping(target = "defectoId", source = "defectoId")
    @Mapping(target = "reclamoId", ignore = true)
    @Mapping(target = "base64", source = "problem", qualifiedByName = "convertirImagen")
    ProblemDto fromProblemToDto(Problem problem);


    default List<ProblemDto> fromListProblemToListDto(List<Problem> problems) {
        return problems.stream().map(this::fromProblemToDto).collect(Collectors.toList());
    }

    @Named("convertirImagen")
    default String convertirImagen(Problem problem) {
        String imagenBase64="";
        if(UtilidadesCadena.noEsNuloNiBlanco(problem.getPictureStringB64()))
         imagenBase64 = PassFileToRepository.imageToBase64(problem.getPictureStringB64(), problem.getExtensionFileP());
        return imagenBase64;

    }

}
