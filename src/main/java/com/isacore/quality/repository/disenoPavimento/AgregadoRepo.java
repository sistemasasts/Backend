package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.Agregado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgregadoRepo extends JpaRepository<Agregado, Long> {
    List<Agregado> findByActivoTrueOrderByNombreAsc();
}
