package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.Defecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDefectoRepo extends JpaRepository<Defecto, Long> {

    List<Defecto> findByActivoTrueOrderByNombreAsc();
}
