package com.isacore.quality.service.reclamoMP;

import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintEstado;
import com.isacore.quality.model.reclamoMP.ComplaintHistorial;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;

import java.util.List;

public interface IComplaintHistorialService {

    void agregar(Complaint salidaMaterial, ComplaintEstado estado, ComplaintOrdenFlujo ordenFlujo, String observacion);

    List<ComplaintHistorial> buscarHistorial(long reclamoId);

}
