package com.isacore.quality.service.cardex;

import com.isacore.quality.model.cardex.InventarioProductoDetalleDto;
import com.isacore.quality.model.cardex.InventarioProductoDto;

import java.util.List;

public interface IInventarioProductoService {

    InventarioProductoDto registrar(InventarioProductoDto dto);

    InventarioProductoDto modificar(InventarioProductoDto dto);

    List<InventarioProductoDto> listar();

    InventarioProductoDetalleDto registrarMovimiento(InventarioProductoDetalleDto dto);

    List<InventarioProductoDetalleDto> listarPorInventarioId(long invetarioId);

}
