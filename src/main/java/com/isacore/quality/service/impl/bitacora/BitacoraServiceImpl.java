package com.isacore.quality.service.impl.bitacora;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.bitacora.Bitacora;
import com.isacore.quality.repository.bitacora.IBitacoraRepo;
import com.isacore.quality.service.bitacora.IBitacoraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BitacoraServiceImpl implements IBitacoraService {
    private final IBitacoraRepo bitacoraRepo;

    @Override
    public List<Bitacora> findAll() {
        return this.bitacoraRepo.findAll();
    }

    @Override
    public Bitacora create(Bitacora bitacora) {
        Bitacora nuevaBitacora = new Bitacora(
                bitacora.getOrigen(),
                bitacora.getFechaLote(),
                bitacora.getMaterial(),
                bitacora.getLote(),
                bitacora.getCantidad(),
                bitacora.getUnidad(),
                bitacora.getSeguimiento(),
                bitacora.getAfectacion(),
                bitacora.getMotivo(),
                bitacora.getDescripcion(),
                bitacora.getControl(),
                bitacora.getAlcance(),
                bitacora.getResponsable()
        );
        nuevaBitacora.setProductId(bitacora.getProductId());
        log.info(String.format("Bitacora registrada %s", nuevaBitacora));

        this.bitacoraRepo.save(nuevaBitacora);

        return nuevaBitacora;
    }

    @Override
    public Bitacora findById(Bitacora id) {
        return null;
    }

    @Transactional
    @Override
    public Bitacora update(Bitacora obj) {
        Optional<Bitacora> bitacora = this.bitacoraRepo.findById(obj.getId());
        if (!bitacora.isPresent())
            throw new ConfiguracionErrorException("Bitacora no encontrada");

        bitacora.get().setOrigen(obj.getOrigen());
        bitacora.get().setFechaLote(obj.getFechaLote());
        bitacora.get().setMaterial(obj.getMaterial());
        bitacora.get().setLote(obj.getLote());
        bitacora.get().setCantidad(obj.getCantidad());
        bitacora.get().setUnidad(obj.getUnidad());
        bitacora.get().setSeguimiento(obj.getSeguimiento());
        bitacora.get().setAfectacion(obj.getAfectacion());
        bitacora.get().setMotivo(obj.getMotivo());
        bitacora.get().setDescripcion(obj.getDescripcion());
        bitacora.get().setControl(obj.getControl());
        bitacora.get().setAlcance(obj.getAlcance());
        bitacora.get().setResponsable(obj.getResponsable());
        bitacora.get().setProductId(obj.getProductId());

        log.info(String.format("Bitacora actualizada %s", bitacora));

        return bitacora.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<Bitacora> listarBitacorasActivas() {
        return null;
    }
}
