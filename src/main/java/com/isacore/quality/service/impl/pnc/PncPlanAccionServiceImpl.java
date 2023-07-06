package com.isacore.quality.service.impl.pnc;

import com.isacore.notificacion.servicio.ServicioNotificacionPnc;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.mapper.pnc.PncPlanAccionMapper;
import com.isacore.quality.model.pnc.*;
import com.isacore.quality.repository.pnc.IPncPlanAccionRepo;
import com.isacore.quality.repository.pnc.IPncSalidaMaterialRepo;
import com.isacore.quality.service.pnc.IPncHistorialService;
import com.isacore.quality.service.pnc.IPncPlanAccionService;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PncPlanAccionServiceImpl implements IPncPlanAccionService {

    private final IPncPlanAccionRepo repositorio;
    private final IPncSalidaMaterialRepo salidaMaterialRepo;
    private final IPncHistorialService historialService;
    private final PncPlanAccionMapper mapper;
    private final ServicioNotificacionPnc notificacionPnc;

    @Override
    public List<PncPlanAccionDto> registrar(PncPlanAccionDto dto) {
        PncSalidaMaterial salidaMaterial = this.obtenerSalidaMaterialPorId(dto.getSalidaMaterialId());
        PncPlanAccion planAccion = new PncPlanAccion(
                dto.getFechaInicio(),
                dto.getFechaFin(),
                dto.getDescripcion(),
                salidaMaterial,
                dto.getResponsable(),
                dto.getOrden()
        );
        this.repositorio.save(planAccion);
        log.info(String.format("PNC %s -> Salida Material %s -> Plan de accion guardado %s",
                salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId(), planAccion));
        return this.mapper.map(this.buscarPorSalidaMaterialId(salidaMaterial.getId()));
    }

    @Transactional
    @Override
    public List<PncPlanAccionDto> actualizar(PncPlanAccionDto dto) {
        PncPlanAccion planAccion = this.obtenerPorId(dto.getId());
        planAccion.setDescripcion(dto.getDescripcion());
        planAccion.setFechaInicio(dto.getFechaInicio());
        planAccion.setFechaFin(dto.getFechaFin());
        planAccion.setResponsable(dto.getResponsable());
        planAccion.setOrden(dto.getOrden());
        this.repositorio.save(planAccion);
        log.info(String.format("PNC %s -> Salida Material %s -> Plan de accion actualizado %s",
                planAccion.getSalidaMaterial().getProductoNoConforme().getNumero(), planAccion.getSalidaMaterial().getId(), planAccion));
        return this.mapper.map(this.buscarPorSalidaMaterialId(planAccion.getSalidaMaterial().getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncPlanAccionDto> listarPorSalidaMaterialId(long salidaMaterialId) {
        return this.mapper.map(this.buscarPorSalidaMaterialId(salidaMaterialId));
    }

    @Transactional(readOnly = true)
    @Override
    public PncPlanAccionDto listarPorId(long id) {
        PncPlanAccion planAccion = this.obtenerPorId(id);
        return this.mapper.mapToDto(planAccion);
    }


    @Override
    public List<PncPlanAccionDto> eliminar(long id, long salidaMaterilaId) {
        this.repositorio.deleteById(id);
        log.info(String.format("Plan de accion id= %s eliminado", id));
        return this.mapper.map(this.buscarPorSalidaMaterialId(salidaMaterilaId));
    }

    @Transactional
    @Override
    public void iniciarGestionPlanes(long salidaMaterialId) {
        List<PncPlanAccion> planes = this.buscarPorSalidaMaterialId(salidaMaterialId)
                .stream().sorted(Comparator.comparing(PncPlanAccion::getOrden))
                .collect(Collectors.toList());

        PncPlanAccion plan = planes.stream().findFirst().orElse(null);
        if (plan == null)
            throw new PncErrorException("Plan de acción no encontrado");
        plan.marcarEnTurno();
//        TODO: enviar correo notificación responsable
        this.notificar(plan);

    }

    @Transactional
    @Override
    public void procesar(PncPlanAccionDto dto) {
        List<PncPlanAccion> planes = this.buscarPorSalidaMaterialId(dto.getSalidaMaterialId());
        PncPlanAccion planRecargado = planes.stream().filter(x -> x.getId() == dto.getId()).findFirst().orElse(null);
        if (planRecargado == null)
            throw new PncErrorException("Plan de acción no encontrado");
        switch (dto.getOrdenFlujo()) {
            case PROCESAR_PLAN_ACCION:
                planRecargado.marcarParaValidacion();
                String observacionUnificada = String.format("TAREA %s COMPLETADA :: %s", planRecargado.getOrden(),
                        esNuloOBlanco(dto.getObservacion()) ? "" : dto.getObservacion());
                this.historialService.agregar(planRecargado.getSalidaMaterial(), PncOrdenFlujo.PROCESAR_PLAN_ACCION,
                        observacionUnificada);
                break;
            case VALIDAR_PLAN_ACCION:
                String observacion = "";
//                String usuario = UtilidadesSeguridad.usuarioEnSesion();
                if (dto.isAprobado()) {
                    for (int i = 0; i < planes.size(); i++) {
                        if (planes.get(i).getEstado().equals(EstadoPncPlanAccion.PENDIENTE_REVISION) && planes.get(i).getFechaAprobacion() == null) {
                            if ((i + 1) < planes.size()) {
                                planes.get(i).marcarComoAprobado();
                                planes.get(i + 1).marcarEnTurno();
                                this.notificar(planes.get(i + 1));
                            } else {
                                planes.get(i).marcarComoAprobado();
                            }
                        }
                    }
                    observacion = String.format("TAREA %s APROBADA :: %s", planRecargado.getOrden(),
                            esNuloOBlanco(dto.getObservacion()) ? "" : dto.getObservacion());
                } else {
                    planRecargado.marcarComoRegresado();
                    observacion = String.format("TAREA %s NO APROBADA :: %s", planRecargado.getOrden(),
                            esNuloOBlanco(dto.getObservacion()) ? "" : dto.getObservacion());
                }
                this.historialService.agregar(planRecargado.getSalidaMaterial(), PncOrdenFlujo.VALIDAR_PLAN_ACCION, observacion);
                if (planes.stream().allMatch(x -> x.getEstado().equals(EstadoPncPlanAccion.FINALIZADO))) {
                    planRecargado.getSalidaMaterial().marcarComoCerrada();
                    log.info(String.format("Salida Material cerrada %s ", planRecargado.getSalidaMaterial()));
                }
                break;
            default:
                break;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncPlanAccionDto> listarPorEstado(EstadoPncPlanAccion estado) {
        String usuario = UtilidadesSeguridad.nombreUsuarioEnSesion();
        List<PncPlanAccion> planes = this.repositorio.findByEstadoAndEnTurnoOrderByFechaFin(estado, "x");
        if (estado.equals(EstadoPncPlanAccion.ASIGNADO))
            return this.mapper.mapIncluidoPnc(planes.stream().filter(x -> x.getResponsable().equals(usuario)).collect(Collectors.toList()));
        else if (estado.equals(EstadoPncPlanAccion.PENDIENTE_REVISION)) {
            return this.mapper.mapIncluidoPnc(planes);
        }
        return null;
    }

    @Override
    public void eliminarPorSalidaMaterialId(long salidaMaterialId) {
        this.repositorio.deleteBySalidaMaterial_Id(salidaMaterialId);
        log.info(String.format("Eliminando Planes de Accion de salida de materia id=%s", salidaMaterialId));
    }

    private List<PncPlanAccion> buscarPorSalidaMaterialId(long salidaMaterialId) {
        return this.repositorio.findBySalidaMaterial_Id(salidaMaterialId)
                .stream()
                .sorted(Comparator.comparing(PncPlanAccion::getOrden))
                .collect(Collectors.toList());
    }

    private PncSalidaMaterial obtenerSalidaMaterialPorId(long id) {
        Optional<PncSalidaMaterial> salidaMaterial = this.salidaMaterialRepo.findById(id);
        if (!salidaMaterial.isPresent())
            throw new PncErrorException(String.format("Salida Material %s no encontrada", id));
        return salidaMaterial.get();
    }

    private PncPlanAccion obtenerPorId(long id) {
        Optional<PncPlanAccion> plan = this.repositorio.findById(id);
        if (!plan.isPresent())
            throw new PncErrorException(String.format("Plan de acción %s no encontrado", id));
        return plan.get();
    }

    private void notificar(PncPlanAccion planAccion){
        try {
            this.notificacionPnc.notificarPlanAccionHabilito(planAccion);
        } catch (Exception e) {
            log.error(String.format("Error al notificar PLAN DE ACCION HABILITADO: %s", e));
        }
    }
}
