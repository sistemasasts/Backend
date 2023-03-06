package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.ProductoNoConforme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoNoConformeRepo extends JpaRepository<ProductoNoConforme, Long> {

    @Query(value = "SELECT NEXT VALUE FOR pnc_secuence", nativeQuery = true)
    int secuencialSiguiente();
}
