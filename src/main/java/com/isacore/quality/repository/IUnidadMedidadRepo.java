package com.isacore.quality.repository;

import com.isacore.quality.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUnidadMedidadRepo extends JpaRepository<UnidadMedida, Long> {

    List<UnidadMedida> findByActivoTrue();

    Optional<UnidadMedida> findByAbreviatura(String abreviatura);
}
