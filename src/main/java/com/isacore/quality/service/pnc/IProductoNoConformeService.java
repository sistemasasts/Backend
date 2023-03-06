package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoNoConformeService {

    ProductoNoConforme registrar(ProductoNoConforme dto);

    ProductoNoConforme actualizar(ProductoNoConforme dto);

    Page<PncDTO> listar(Pageable pageabe, ConsultaPncDTO dto);

    boolean anular(ProductoNoConforme dto);

    List<PncDefecto> agregarDefecto(String jsonCriteria, byte[] file, String nombreArchivo, String tipo);

    List<PncDefecto> eliminarDefecto(PncDefecto dto);

    List<PncDefecto> actualizarDefecto(PncDefecto dto);
}
