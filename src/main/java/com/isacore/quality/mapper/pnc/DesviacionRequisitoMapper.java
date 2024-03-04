package com.isacore.quality.mapper.pnc;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoDto;
import com.isacore.quality.model.pnc.LineaAfecta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DesviacionRequisito.class})
public interface DesviacionRequisitoMapper {

    @Mapping(target = "observacion", ignore = true)
    @Mapping(target = "orden", ignore = true)
    @Mapping(target = "accion", ignore = true)
    @Mapping(target = "afectacionText", source = "afectacion", qualifiedByName = "afectacionDescripcion")
    @Mapping(target = "productTypeText", source = "product", qualifiedByName = "productoTipoTexto")
    @Mapping(target = "productoNombre", source = "product", qualifiedByName = "productoNombre")
    DesviacionRequisitoDto fromDesviacionRequisitoToDto(DesviacionRequisito desviacionRequisito);

    List<DesviacionRequisitoDto> fromListDesviacionRequisitoToDto(List<DesviacionRequisito> desviacionRequisito);

    @Named("afectacionDescripcion")
    default String afectacionDescripcion(LineaAfecta afectacion) {
        return afectacion != null ? afectacion.getDescripcion() : "";
    }

    @Named("productoTipoTexto")
    default String productoTipoTexto(Product producto) {
        return producto != null ? producto.getTypeProduct().getDescripcion() : "";
    }

    @Named("productoNombre")
    default String productoNombre(Product producto) {
        return producto != null ? producto.getNameProduct() : "";
    }
}
