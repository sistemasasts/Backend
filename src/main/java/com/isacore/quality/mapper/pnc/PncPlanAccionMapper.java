package com.isacore.quality.mapper.pnc;

import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PncPlanAccionMapper {

    private final IUserImptekRepo usuarioRepo;

    public PncPlanAccionDto mapToDto(PncPlanAccion valor) {
        return PncPlanAccionDto.builder()
                .id(valor.getId())
                .estado(valor.getEstado())
                .descripcion(valor.getDescripcion())
                .fechaInicio(valor.getFechaInicio())
                .fechaFin(valor.getFechaFin())
                .orden(valor.getOrden())
                .responsable(valor.getResponsable())
                .responsableNombre(this.nombreUsuario(valor.getResponsable()))
                .llenarInfoAdicional(valor.isLlenarInfoAdicional())
                .build();
    }

    public PncPlanAccionDto mapToDtoIncluidoPnc(PncPlanAccion valor) {
        return PncPlanAccionDto.builder()
                .id(valor.getId())
                .estado(valor.getEstado())
                .descripcion(valor.getDescripcion())
                .fechaInicio(valor.getFechaInicio())
                .fechaFin(valor.getFechaFin())
                .orden(valor.getOrden())
                .responsable(valor.getResponsable())
                .responsableNombre(this.nombreUsuario(valor.getResponsable()))
                .salidaMaterialId(valor.getSalidaMaterial().getId())
                .numeroPnc(valor.getSalidaMaterial().getProductoNoConforme().getNumero())
                .nombreProducto(valor.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct())
                .cantidad(valor.getSalidaMaterial().getCantidad())
                .unidad(valor.getSalidaMaterial().getProductoNoConforme().getUnidad().getAbreviatura())
                .destino(valor.getSalidaMaterial().getDestino().getDescripcion())
                .lote(valor.getSalidaMaterial().getProductoNoConforme().getLote())
                .llenarInfoAdicional(valor.isLlenarInfoAdicional())
                .build();
    }

    public List<PncPlanAccionDto> map(List<PncPlanAccion> list) {
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<PncPlanAccionDto> mapIncluidoPnc(List<PncPlanAccion> list) {
        return list.stream().map(this::mapToDtoIncluidoPnc).collect(Collectors.toList());
    }

    private String nombreUsuario(String usuario) {
        Optional<UserImptek> usuarioOP = this.usuarioRepo.findByIdUser(usuario);
        return usuarioOP.isPresent() ? usuarioOP.get().getEmployee().getCompleteName() : usuario;
    }
}
