package com.isacore.quality.service.impl.pnc;

import com.isacore.quality.model.pnc.*;
import com.isacore.quality.repository.pnc.IPncSalidaMaterialRepo;
import com.isacore.quality.repository.pnc.IProductoNoConformeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ValidadorPncFinalizar {

    private final IPncSalidaMaterialRepo pncSalidaMaterialRepositorio;
    private final IProductoNoConformeRepo productoNoConformeRepositorio;

    public void verificarSiFinalizaPNC(ProductoNoConforme productoNoConforme) {
        List<PncDefecto> defectos = productoNoConforme.getDefectos();
        BigDecimal stock = defectos.stream().map(PncDefecto::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<PncSalidaMaterial> salidaMateriales = pncSalidaMaterialRepositorio.findByProductoNoConforme_Id(productoNoConforme.getId());
        if (stock.compareTo(BigDecimal.ZERO) <= 0 && salidaMateriales.stream().allMatch(x -> x.getEstado().equals(EstadoSalidaMaterial.CERRADO))) {
            productoNoConforme.setEstado(EstadoPnc.FINALIZADO);
            productoNoConformeRepositorio.save(productoNoConforme);
        }
    }
}
