package com.isacore.quality.mapper.cardex;

import com.isacore.quality.model.cardex.InventarioProducto;
import com.isacore.quality.model.cardex.InventarioProductoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioProductoMapper {

    public InventarioProductoDto mapToDto(InventarioProducto valor) {
        return InventarioProductoDto.builder()
                .id(valor.getId())
                .productoId(valor.getProducto().getIdProduct())
                .productoNombre(valor.getProducto().getNameProduct())
                .tipoProducto(valor.getProducto().getTypeProductTxt())
                .minimo(valor.getMinimo())
                .maximo(valor.getMaximo())
                .cantidadAlertar(valor.getCantidadAlertar())
                .stock(valor.getStock())
                .unidad(valor.getUnidad())
                .build();
    }

    public List<InventarioProductoDto> map(List<InventarioProducto> inventarioProductos){
        return inventarioProductos.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
