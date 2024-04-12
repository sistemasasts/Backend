package com.isacore.quality.service.reclamoMP;

import com.isacore.quality.model.reclamoMP.*;

import java.util.List;

public interface IComplaintHistorialService {

    void agregar(Complaint salidaMaterial, ComplaintEstado estado, ComplaintOrdenFlujo ordenFlujo, String observacion);

    void agregar(Complaint salidaMaterial, ComplaintEstado estado, ComplaintOrdenFlujo ordenFlujo, String observacion, ProviderActionPlan planAccion);

    List<ComplaintHistorial> buscarHistorial(long reclamoId);

}
