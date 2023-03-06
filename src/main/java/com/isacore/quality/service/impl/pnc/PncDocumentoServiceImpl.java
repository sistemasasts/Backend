package com.isacore.quality.service.impl.pnc;

import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.model.pnc.PncDocumento;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.pnc.ProductoNoConforme;
import com.isacore.quality.repository.pnc.IPncDocumentoRepo;
import com.isacore.quality.service.pnc.IPncDocumentoService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.util.PassFileToRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Slf4j
@Service
public class PncDocumentoServiceImpl implements IPncDocumentoService {

    private final IPncDocumentoRepo repositorio;
    private final ConfiguracionSolicitud configuracion;

    @Transactional
    @Override
    public PncDocumento registrar(ProductoNoConforme productoNoConforme, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden) {
        try {
            String path = this.crearPathArchivo(productoNoConforme.getId(), null, nombreArchivo);
            PassFileToRepository.saveLocalFile(path, bytes);
            PncDocumento documento = new PncDocumento(
                    productoNoConforme.getId(),
                    null,
                    null,
                    path,
                    null,
                    nombreArchivo,
                    orden,
                    tipo
            );
            this.repositorio.save(documento);
            log.info(String.format("PNC %s -> Documento guardado %s", productoNoConforme.getId(), documento));
            return documento;

        } catch (IOException e) {
            log.error(String.format("PNC %s -> Error al subir Documento %s", productoNoConforme.getId(), e.getMessage()));
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Transactional
    @Override
    public PncDocumento registrar(ProductoNoConforme productoNoConforme, PncSalidaMaterial salidaMaterial, byte[] bytes,
                                  String nombreArchivo, String tipo, PncOrdenFlujo orden) {
        try {
            String path = this.crearPathArchivo(productoNoConforme.getId(), null, nombreArchivo);
            PassFileToRepository.saveLocalFile(path, bytes);
            PncDocumento documento = new PncDocumento(
                    productoNoConforme.getId(),
                    salidaMaterial.getId(),
                    null,
                    path,
                    salidaMaterial.getEstado(),
                    nombreArchivo,
                    orden,
                    tipo
            );
            this.repositorio.save(documento);
            log.info(String.format("PNC %s : Salida Material -> Documento guardado %s", productoNoConforme.getId(), documento));
            return documento;

        } catch (IOException e) {
            log.error(String.format("PNC %s : Salida Material -> Error al subir Documento %s", productoNoConforme.getId(), e.getMessage()));
            throw new ApprobationCriteriaErrorException();
        }
    }

    private String crearPathArchivo(long pncSecuencial, Long pncSalidaMaterial, String nombreArchivo) {

        String path = crearRutaAlmacenamiento(pncSecuencial, pncSalidaMaterial).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(pncSecuencial, pncSalidaMaterial).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);
        log.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
        return path;
    }

    private String crearRutaAlmacenamiento(long pncSecuencial, Long pncSalidaMaterial) {
        try {
            String carpeta = configuracion.getRutaBase().concat(File.separator).concat("PNC").concat(File.separator).concat(String.valueOf(pncSecuencial));
            if (pncSalidaMaterial != null)
                carpeta = carpeta.concat(File.separator).concat("SALIDA_MATERIAL").concat(String.valueOf(pncSalidaMaterial));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectory(path);
            return carpeta;
        } catch (IOException e) {
            log.error(String.format("Error al subir Documento %s", e.getMessage()));
            throw new PncErrorException("Error al crear el directorio");
        }
    }


}
