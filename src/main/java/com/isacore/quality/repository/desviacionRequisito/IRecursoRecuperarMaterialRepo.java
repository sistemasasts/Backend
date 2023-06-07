package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.RecursoRecuperarMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecursoRecuperarMaterialRepo extends JpaRepository<RecursoRecuperarMaterial, Long> {

    List<RecursoRecuperarMaterial> findByDesviacionRequisito_Id(long id);
}
