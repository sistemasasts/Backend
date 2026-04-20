package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.TipoLigante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoLiganteRepo extends JpaRepository<TipoLigante, Long>{
    List<TipoLigante> findByActivoTrueOrderByNombreAsc();
}
