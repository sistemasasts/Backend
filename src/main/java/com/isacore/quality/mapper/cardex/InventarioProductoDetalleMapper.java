package com.isacore.quality.mapper.cardex;

import com.isacore.quality.model.cardex.InventarioProductoDetalle;
import com.isacore.quality.model.cardex.InventarioProductoDetalleDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioProductoDetalleMapper {

    public InventarioProductoDetalleDto mapToDto(InventarioProductoDetalle valor) {
        return InventarioProductoDetalleDto.builder()
                .id(valor.getId())
                .responsable(valor.getResponsableNombreCompleto())
                .cantidad(valor.getCantidad())
                .fechaEnsayo(valor.getFechaEnsayo())
                .numeroEnsayo(valor.getNumeroEnsayo())
                .stockActual(valor.getStockActual())
                .tipoMovimiento(valor.getTipoMovimiento())
                .inventarioProductoId(valor.getInventarioProducto().getId())
                .build();
    }

    public List<InventarioProductoDetalleDto> map(List<InventarioProductoDetalle> inventarioProductos) {
        return inventarioProductos.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
