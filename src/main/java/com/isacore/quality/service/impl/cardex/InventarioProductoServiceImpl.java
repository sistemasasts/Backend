package com.isacore.quality.service.impl.cardex;

import com.isacore.quality.exception.CardexErrorException;
import com.isacore.quality.exception.UsuarioErrorException;
import com.isacore.quality.mapper.cardex.InventarioProductoDetalleMapper;
import com.isacore.quality.mapper.cardex.InventarioProductoMapper;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.cardex.*;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.repository.cardex.IInventarioProductoDetalleRepo;
import com.isacore.quality.repository.cardex.IInventarioProductoRepo;
import com.isacore.quality.service.cardex.IInventarioProductoService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventarioProductoServiceImpl implements IInventarioProductoService {

    private final IInventarioProductoRepo repositorio;
    private final IInventarioProductoDetalleRepo repositorioDetalle;
    private final IProductRepo productoRepositorio;
    private final IUserImptekRepo usuarioRepositorio;
    private final InventarioProductoMapper mapper;
    private final InventarioProductoDetalleMapper mapperDetalle;

    @Transactional
    @Override
    public InventarioProductoDto registrar(InventarioProductoDto dto) {
        Product producto = this.obtenerProducto(dto.getProductoId());
        InventarioProducto inventario = new InventarioProducto(
                producto,
                dto.getMinimo(),
                dto.getMaximo(),
                dto.getCantidadAlertar(),
                dto.getUnidad()
        );
        this.repositorio.save(inventario);
        log.info(String.format("Inventario producto guardado %s", inventario));
        return this.mapper.mapToDto(inventario);
    }

    @Transactional
    @Override
    public InventarioProductoDto modificar(InventarioProductoDto dto) {
        InventarioProducto inventario = this.repositorio.findById(dto.getId()).orElse(null);
        if (inventario == null)
            throw new CardexErrorException("Inventario producto no encontrado");
        inventario.setMaximo(dto.getMaximo());
        inventario.setMinimo(dto.getMinimo());
        inventario.setUnidad(dto.getUnidad());
        log.info(String.format("Inventario producto actualizado %s", inventario));
        return this.mapper.mapToDto(inventario);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InventarioProductoDto> listar() {
        List<InventarioProducto> detalle = this.repositorio.findAll();
        return this.mapper.map(detalle);
    }

    @Transactional
    @Override
    public InventarioProductoDetalleDto registrarMovimiento(InventarioProductoDetalleDto dto) {
        InventarioProducto inventarioProducto = this.repositorio.findById(dto.getInventarioProductoId()).orElse(null);
        if (inventarioProducto == null)
            throw new CardexErrorException("Inventario producto no encontrado");

        this.validarPuedeRegistrarConsumo(inventarioProducto, dto.getCantidad());
        UserImptek usuario = this.obtenerUsuario();
        BigDecimal stockActualizado = this.calcularStock(dto.getTipoMovimiento(), inventarioProducto.getStock(), dto.getCantidad());
        InventarioProductoDetalle detalle = new InventarioProductoDetalle(
                usuario.getIdUser(),
                usuario.getEmployee().getCompleteName(),
                dto.getCantidad(),
                dto.getFechaEnsayo(),
                dto.getNumeroEnsayo(),
                stockActualizado,
                dto.getTipoMovimiento(),
                inventarioProducto
        );
        inventarioProducto.setStock(stockActualizado);
        this.repositorioDetalle.save(detalle);
        log.info(String.format("Inventario movimiento registrado %s", detalle));
        return this.mapperDetalle.mapToDto(detalle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InventarioProductoDetalleDto> listarPorInventarioId(long invetarioId) {
        return null;
    }

    private BigDecimal calcularStock(TipoMovimiento tipoMovimiento, BigDecimal stock, BigDecimal cantidad) {
        switch (tipoMovimiento) {
            case INGRESO:
                return stock.add(cantidad);
            case EGRESO:
                return stock.subtract(cantidad);
            default:
                return stock;
        }
    }

    private void validarPuedeRegistrarConsumo(InventarioProducto inventarioProducto, BigDecimal cantidad) {
        if (inventarioProducto.getStock().compareTo(cantidad) < 0)
            throw new CardexErrorException("Cantidad a consumir superior al stock actual");
    }

    private Product obtenerProducto(Integer id) {
        Optional<Product> producto = this.productoRepositorio.findById(id);
        if (!producto.isPresent())
            throw new CardexErrorException("Producto no encontrado");
        return producto.get();
    }

    private UserImptek obtenerUsuario() {
        String nombreUsuario = UtilidadesSeguridad.nombreUsuarioEnSesion();
        UserImptek usuario = this.usuarioRepositorio.findOneByNickName(nombreUsuario);
        if (usuario == null)
            throw new UsuarioErrorException(String.format("Usuario %s no encontrado", nombreUsuario));
        return usuario;
    }
}
