package com.isacore.quality.repository;

import com.isacore.quality.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUnidadMedidadRepo extends JpaRepository<UnidadMedida, String> {

    List<UnidadMedida> findByActivoTrue();
}
