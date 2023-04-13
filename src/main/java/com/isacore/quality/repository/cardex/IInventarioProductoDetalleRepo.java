package com.isacore.quality.repository.cardex;

import com.isacore.quality.model.cardex.InventarioProductoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInventarioProductoDetalleRepo extends JpaRepository<InventarioProductoDetalle, Long>{

}
