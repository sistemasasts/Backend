package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.Mina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinaRepo extends JpaRepository<Mina, Long> {
    List<Mina> findByActivoTrueOrderByNombreAsc();
}
