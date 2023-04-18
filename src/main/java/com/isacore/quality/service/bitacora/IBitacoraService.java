package com.isacore.quality.service.bitacora;

import com.isacore.quality.model.bitacora.Bitacora;
import com.isacore.util.CRUD;

import java.util.List;

public interface IBitacoraService extends CRUD<Bitacora> {
    List<Bitacora> listarBitacorasActivas();
}
