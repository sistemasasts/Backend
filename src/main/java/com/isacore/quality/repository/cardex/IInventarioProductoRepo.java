package com.isacore.quality.repository.cardex;

import com.isacore.quality.model.cardex.InventarioProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInventarioProductoRepo extends JpaRepository<InventarioProducto, Long>{

}
