package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.*;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDesviacionRequisitoService extends CRUD<DesviacionRequisito> {
    List<DesviacionRequisito> listarDesviacionRequistoActivos();

    DesviacionRequisito listarDesviacionRequisitosPorId(Long desviacionId);

    byte[] generarReporte(Long id);

    Page<DesviacionRequisito> listar(Pageable pageabe, ConsultaDesviacionRequisitoDTO dto);

    List<DesviacionRequisitoDefecto> agregarDefecto(long id, long defectoId);

    List<DesviacionRequisitoDefecto> eliminarDefecto(long id, long defectoId);

    void enviarAprobacion(DesviacionRequisitoDto dto);

    void procesar(DesviacionRequisitoDto dto);

    List<DesviacionRequisitoDto> listarPorEstado(EstadoDesviacion estado);
}
