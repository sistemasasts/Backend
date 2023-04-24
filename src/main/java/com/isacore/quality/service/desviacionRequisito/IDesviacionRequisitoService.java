package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.ConsultaDesviacionRequisitoDTO;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.pnc.ConsultaPncDTO;
import com.isacore.quality.model.pnc.PncDTO;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDesviacionRequisitoService extends CRUD<DesviacionRequisito> {
    List<DesviacionRequisito> listarDesviacionRequistoActivos();

    DesviacionRequisito listarDesviacionRequisitosPorId(Long desviacionId);

    byte[] generarReporte(Long id);

    Page<DesviacionRequisito> listar(Pageable pageabe, ConsultaDesviacionRequisitoDTO dto);
}
