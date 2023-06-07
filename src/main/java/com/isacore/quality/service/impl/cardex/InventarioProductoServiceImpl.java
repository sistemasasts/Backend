package com.isacore.quality.service.impl.cardex;

import com.isacore.notificacion.servicio.ServicioNotificacionInventario;
import com.isacore.quality.exception.CardexErrorException;
import com.isacore.quality.exception.UsuarioErrorException;
import com.isacore.quality.mapper.cardex.InventarioProductoDetalleMapper;
import com.isacore.quality.mapper.cardex.InventarioProductoMapper;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.cardex.*;
import com.isacore.quality.model.pnc.ConsultaPncDTO;
import com.isacore.quality.model.pnc.PncDTO;
import com.isacore.quality.model.pnc.ProductoNoConforme;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.repository.cardex.IInventarioProductoDetalleRepo;
import com.isacore.quality.repository.cardex.IInventarioProductoRepo;
import com.isacore.quality.service.cardex.IInventarioProductoService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final EntityManager entityManager;
    private final ServicioNotificacionInventario servicioNotificacionInventario;

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
    public InventarioProductoDto listarPorId(long id) {
        InventarioProducto inventario = this.repositorio.findById(id).orElse(null);
        if (inventario == null)
            throw new CardexErrorException("Inventario producto no encontrado");
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

        this.validarPuedeRegistrarConsumo(inventarioProducto, dto.getCantidad(), dto.getTipoMovimiento());
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
        if(stockActualizado.compareTo(inventarioProducto.getMinimo()) <=0){
            try{
                this.servicioNotificacionInventario.notificarNecesitaCompra(inventarioProducto);
            }catch (Exception e){
                log.error(String.format("Error al enviar la notificacion de minimo stock %s", e));
            }
        }
        log.info(String.format("Inventario movimiento registrado %s", detalle));
        return this.mapperDetalle.mapToDto(detalle);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<InventarioProductoDetalleDto> listarPorInventarioId(Pageable pageable, IventarioFiltrosDto dto) {
        try {
            List<InventarioProductoDetalle> respuesta = obtenerInventarioDetallePorCriterios(dto);
            final int sizeTotal = respuesta.size();
            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), respuesta.size());
            respuesta = respuesta.subList(start, end);
            final Page<InventarioProductoDetalleDto> pageResut = new PageImpl<>(this.mapperDetalle.map(respuesta), pageable, sizeTotal);
            return pageResut;
        } catch (Exception e) {
            final Page<InventarioProductoDetalleDto> pageResult = new PageImpl<InventarioProductoDetalleDto>(new ArrayList<InventarioProductoDetalleDto>(), pageable, 0);
            return pageResult;
        }
    }

    private List<InventarioProductoDetalle> obtenerInventarioDetallePorCriterios(IventarioFiltrosDto consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<InventarioProductoDetalle> query = criteriaBuilder.createQuery(InventarioProductoDetalle.class);
            final Root<InventarioProductoDetalle> root = query.from(InventarioProductoDetalle.class);
            final List<Predicate> predicadosConsulta = new ArrayList<>();
            if (consulta.getInventarioId() > 0)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("inventarioProducto").get("id"), consulta.getInventarioId()));
            else
                throw new CardexErrorException("Debe especificar el producto para consultar el detalle");

//            if (consulta.getFechaInicio() == null && consulta.getFechaFin() == null) {
//                LocalDateTime fechaFin = LocalDateTime.now();
//                consulta.setFechaFin(fechaFin);
//                LocalDateTime fechaInicio =  fechaFin.minusDays(30);
//                consulta.setFechaInicio(fechaInicio);
//            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
                predicadosConsulta.add(criteriaBuilder.between(root.get("fechaRegistro"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                        consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
                predicadosConsulta.add(criteriaBuilder.between(root.get("fechaRegistro"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0), LocalDateTime.now()));
            }

            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.asc(root.get("fechaRegistro")));
            final TypedQuery<InventarioProductoDetalle> statement = this.entityManager.createQuery(query);

            final List<InventarioProductoDetalle> cotizacionesResult = statement.getResultList();
            return cotizacionesResult;
        } catch (Exception e) {
            log.error(String.format("Error al consultar InventarioProductoDetalle %s", e.getMessage()));
            return new ArrayList<>();
        }
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

    private void validarPuedeRegistrarConsumo(InventarioProducto inventarioProducto, BigDecimal cantidad, TipoMovimiento movimiento) {
        if (movimiento.equals(TipoMovimiento.EGRESO))
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
