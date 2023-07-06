package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncDefecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPncDefectoRepo extends JpaRepository<PncDefecto, Long> {

    @Query(value = "select top 1 A.* from pnc_defecto (nolock) A inner join pnc_salida_material(nolock) B \n" +
            "on (A.id = B.pnc_defecto_id) where A.id= :id", nativeQuery = true)
    PncDefecto tieneAlmenosUnaSalidaMaterial(@Param("id") Long id);

}
