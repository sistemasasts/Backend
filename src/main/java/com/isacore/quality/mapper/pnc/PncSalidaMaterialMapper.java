package com.isacore.quality.mapper.pnc;

import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterialDto;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PncSalidaMaterialMapper {

    private final IUserImptekRepo usuarioRepo;

    public PncSalidaMaterialDto mapToDto(PncSalidaMaterial valor) {
        return PncSalidaMaterialDto.builder()
                .id(valor.getId())
                .cantidad(valor.getCantidad())
                .fecha(valor.getFecha())
                .destino(valor.getDestino())
                .estado(valor.getEstado())
                .observacion(valor.getObservacion())
                .idPnc(valor.getProductoNoConforme().getId())
                .cantidadPnc(valor.getProductoNoConforme().getCantidadNoConforme())
                .unidad(valor.getProductoNoConforme().getUnidad().getAbreviatura())
                .lote(valor.getProductoNoConforme().getLote())
                .numero(valor.getProductoNoConforme().getNumero())
                .producto(valor.getProductoNoConforme().getProducto().getNameProduct())
                .fechaCreacion(valor.getProductoNoConforme().getFechaCreacion())
                .saldoPnc(valor.getProductoNoConforme().getSaldo())
                .solicitante(this.nombreUsuario(valor.getUsuario()))
                .idPncDefecto(valor.getPncDefecto().getId())
                .fechaAprobacion(valor.getFechaAprobacion())
                .usuarioAprobador(valor.getUsuarioAprobador())
                .verPlanesAccion(valor.verPlanesAccion())
                .build();
    }

    public List<PncSalidaMaterialDto> map(List<PncSalidaMaterial> list) {
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private String nombreUsuario(String usuario) {
        Optional<UserImptek> usuarioOP = this.usuarioRepo.findByIdUser(usuario);
        return usuarioOP.isPresent() ? usuarioOP.get().getEmployee().getCompleteName() : usuario;
    }
}
