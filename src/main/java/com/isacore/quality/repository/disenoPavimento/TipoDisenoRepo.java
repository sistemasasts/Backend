package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.TipoDiseno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoDisenoRepo extends JpaRepository<TipoDiseno, Long>{
    List<TipoDiseno> findByActivoTrueOrderByNombreAsc();
}
