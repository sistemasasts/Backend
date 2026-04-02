package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.AgregadoQuimica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgregadoQuimicaRepo extends JpaRepository<AgregadoQuimica, Long> {
    List<AgregadoQuimica> findByActivoTrueOrderByNombreAsc();
}
