package com.isacore.quality.mapper.reclamoMP;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintDto;
import com.isacore.quality.model.reclamoMP.ComplaintEstado;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.repository.IProviderRepo;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import com.isacore.util.StaticInjector;
import com.isacore.util.UtilidadesCadena;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {ProblemaMapper.class, ReclamoAccionEjecutadasMapper.class, ReclamoPlanesAccionMapper.class})
public interface ReclamoMapper {

    @Mapping(target = "observacion", ignore = true)
    @Mapping(target = "orden", ignore = true)
    @Mapping(target = "accion", ignore = true)
    @Mapping(target = "destinatarios", ignore = true)
    @Mapping(target = "mensaje", ignore = true)
    @Mapping(target = "asunto", ignore = true)
    @Mapping(target = "problemas", source = "listProblems")
    @Mapping(target = "listActionsPlanProvider", source = "listActionsPlanProvider")
    @Mapping(target = "unidadMedidaId", source = "unit", qualifiedByName = "unidadMedidaId")
    @Mapping(target = "unidadMedidaDescripcion", source = "unit", qualifiedByName = "unidadMedidaDescripcion")
    @Mapping(target = "nombreProducto", source = "idProduct", qualifiedByName = "productoNombre")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "dateCreateComplaint", source = "dateCreateComplaint")
    @Mapping(target = "estadoTexto", source = "state", qualifiedByName = "estadoTexto")
    @Mapping(target = "nombreProveedor", source = "idProvider", qualifiedByName = "proveedorNombre")
    @Mapping(target = "aprobadorCalidadCompleto", source = "aprobadorCalidad", qualifiedByName = "aprobadorCalidadNombre")
    @Mapping(target = "aprobadorComprasCompleto", source = "aprobadorCompras", qualifiedByName = "aprobadorComprasNombre")
    ComplaintDto fromReclamoToDto(Complaint complaint);

    @Mapping(target = "observacion", ignore = true)
    @Mapping(target = "orden", ignore = true)
    @Mapping(target = "accion", ignore = true)
    @Mapping(target = "problemas", ignore = true)
    @Mapping(target = "destinatarios", ignore = true)
    @Mapping(target = "mensaje", ignore = true)
    @Mapping(target = "asunto", ignore = true)
    @Mapping(target = "unidadMedidaId", source = "unit", qualifiedByName = "unidadMedidaId")
    @Mapping(target = "unidadMedidaDescripcion", source = "unit", qualifiedByName = "unidadMedidaDescripcion")
    @Mapping(target = "nombreProducto", source = "idProduct", qualifiedByName = "productoNombre")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "estadoTexto", source = "state", qualifiedByName = "estadoTexto")
    @Mapping(target = "nombreProveedor", source = "idProvider", qualifiedByName = "proveedorNombre")
    @Mapping(target = "aprobadorCalidadCompleto", ignore = true)
    @Mapping(target = "aprobadorComprasCompleto", ignore = true)
    @Mapping(target = "kpiTime", source = "complaint", qualifiedByName = "indicador")
    ComplaintDto fromReclamoBasicoToDto(Complaint complaint);

    default List<ComplaintDto> fromListReclamoToListDto(List<Complaint> honorarios) {
        return honorarios.stream().map(this::fromReclamoBasicoToDto).collect(Collectors.toList());
    }

    @Named("productoNombre")
    default String productoNombre(Integer productoId) {
        final IProductRepo repo = StaticInjector.getInstance().getBean(IProductRepo.class);
        Product product = repo.findById(productoId).orElse(null);
        return product == null ? "": product.getNameProduct();
    }

    @Named("unidadMedidaId")
    default long unidadMedidaId(UnidadMedida unidadMedida) {
        return unidadMedida == null ? 0: unidadMedida.getId();
    }

    @Named("unidadMedidaDescripcion")
    default String unidadMedidaDescripcion(UnidadMedida unidadMedida) {
        return unidadMedida == null ? "": unidadMedida.getAbreviatura();
    }

    @Named("estadoTexto")
    default String estadoTexto(ComplaintEstado estado) {
        return estado.getDescripcion();
    }

    @Named("proveedorNombre")
    default String proveedorNombre(Integer productoId) {
        if(productoId == null)
            return "";
        final IProviderRepo repo = StaticInjector.getInstance().getBean(IProviderRepo.class);
        Provider product = repo.findById(productoId).orElse(null);
        return product == null ? "": product.getNameProvider();
    }

    @Named("aprobadorCalidadNombre")
    default String aprobadorCalidadNombre(String calidad) {
        if(UtilidadesCadena.esNuloOBlanco(calidad))
            return "";
        final UsuarioRepositorio repo = StaticInjector.getInstance().getBean(UsuarioRepositorio.class);
        Usuario product = repo.findByNombreUsuario(calidad).orElse(null);
        return product == null ? "": product.getNombre();
    }

    @Named("aprobadorComprasNombre")
    default String aprobadorComprasNombre(String compras) {
        if(UtilidadesCadena.esNuloOBlanco(compras))
            return "";
        final UsuarioRepositorio repo = StaticInjector.getInstance().getBean(UsuarioRepositorio.class);
        Usuario product = repo.findByNombreUsuario(compras).orElse(null);
        return product == null ? "": product.getNombre();
    }

    @Named("indicador")
    default Long indicador(Complaint complaint) {
        return complaint.getKpiTime();
    }
}

