package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.PncDocumento;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.pnc.ProductoNoConforme;

public interface IPncDocumentoService {

    PncDocumento registrar(ProductoNoConforme productoNoConforme, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);

    PncDocumento registrar(ProductoNoConforme productoNoConforme, PncSalidaMaterial salidaMaterial, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);
}
