package com.isacore.quality.service.cardex;

import com.isacore.quality.model.cardex.InventarioProductoDetalleDto;
import com.isacore.quality.model.cardex.InventarioProductoDto;
import com.isacore.quality.model.cardex.IventarioFiltrosDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInventarioProductoService {

    InventarioProductoDto registrar(InventarioProductoDto dto);

    InventarioProductoDto modificar(InventarioProductoDto dto);

    InventarioProductoDto listarPorId(long id);

    List<InventarioProductoDto> listar();

    InventarioProductoDetalleDto registrarMovimiento(InventarioProductoDetalleDto dto);

    Page<InventarioProductoDetalleDto> listarPorInventarioId(Pageable pageabe, IventarioFiltrosDto dto);
}
