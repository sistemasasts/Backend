package com.isacore.quality.service.reclamoMP;

import com.isacore.quality.model.reclamoMP.*;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IComplaintService extends CRUD<ComplaintDto> {
	
	void close(ComplaintDto dto);

	Page<ComplaintDto> listarPorCriterio(Pageable pageabe, ConsultaReclamoDto dto);

	ComplaintDto buscarPorId(long id);

	List<ProblemDto> agregarProblema(String jsonDTO, byte[] file, String nombreArchivo, String tipo);

	List<ProblemDto> listarProblemas(long complaint);

	List<ProblemDto> eliminarProblema(long reclamoId, long problemaId);

	void enviarReclamo(ComplaintDto dto);

	void procesarReclamoCalidad(ComplaintDto dto);

	void procesarReclamoCompras(ComplaintDto dto);

	void anular(ComplaintDto dto);

	List<ComplaintDto> listarAsignadasPorEstado(ComplaintEstado estado);

	byte[] generateReporte(long id);

	List<ProviderActionPlanDto> agregarPLanAccion(ProviderActionPlanDto dto);

	List<ProviderActionPlanDto> actualizarPLanAccion(ProviderActionPlanDto dto);

	List<ProviderActionPlanDto> eliminarPlanAccion(long reclamoId, long planAccionId, ProviderActionPlanDto dto);

	List<ExecutedActionDto> agregarAccionEjecutada(ExecutedActionDto dto);

	List<ExecutedActionDto> actualizarAccionEjecutada(ExecutedActionDto dto);

	List<ExecutedActionDto> eliminarAccionEjecutada(long reclamoId, long planAccionId);

	void notificarReclamo(ComplaintDto dto);

	void enviarPlanesAccion(ComplaintDto dto);

	List<ComplaintDto> listarPorPlanerAccionPorUsuarioSesion();

	List<ProviderActionPlanDto> procesarPlanAccion(ProviderActionPlanDto dto);

	List<ProviderActionPlanDto> validarPlanAccion(ProviderActionPlanDto dto);

}
