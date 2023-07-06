package com.isacore.quality.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.EmailDto;
import com.isacore.quality.dto.ReportDto;
import com.isacore.quality.exception.*;
import com.isacore.quality.model.*;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.quality.model.se.SolicitudDocumentoDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.repository.IHccHeadRepo;
import com.isacore.quality.service.IHccHeadService;
import com.isacore.quality.service.IReportHeadTService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import com.isacore.util.WebResponseMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class HccHeadServiceImpl implements IHccHeadService {

    private static final Log LOG = LogFactory.getLog(HccHeadServiceImpl.class);
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private IHccHeadRepo repo;
    @Autowired
    private IUserImptekRepo repoUsuario;
    @Autowired
    private ConfiguracionSolicitud configuracion;
    @Autowired
    private IReportHeadTService serviceRH;

    @Override
    public List<HccHead> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HccHead create(HccHead hcc) {
        return this.repo.save(hcc);
    }

    @Override
    public HccHead findById(HccHead hh) {
        Optional<HccHead> hccH = this.repo.findById(hh.getId());
        return hccH.get();
    }

    @Override
    public HccHead update(HccHead hcc) {
        return this.repo.save(hcc);
    }

    @Override
    public boolean delete(String id) {
        return true;

    }

    @Override
    public List<HccHead> findOnlyHccHead(String tp) {

        List<Object[]> list = this.repo.findOnlyHccHead(tp);

        if (list.isEmpty() || list == null)
            return null;
        else {
            List<HccHead> listhcc = new ArrayList<>();

            list.forEach((Object[] x) -> {
                HccHead hh = new HccHead();
                Product p = new Product();
                hh.setId(((BigInteger) x[0]).longValue());
                hh.setHcchBatch((String) x[1]);
                p.setNameProduct((String) x[2]);
                p.setTypeProduct(ProductType.valueOf((String) x[3]));
                hh.setProduct(p);
                Instant instant = Instant.ofEpochMilli(((Date) x[4]).getTime());
                hh.setDateCreate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
                hh.setAnalysis((String) x[5]);
                hh.setSapCode((String) x[6]);
                hh.setPeriodicity((String) x[7]);
                listhcc.add(hh);
            });
            return listhcc;
        }
    }

    @Override
    public List<HccHead> findOnlyHccHead() {
        List<Object[]> list = this.repo.findOnlyHccHead();

        if (list.isEmpty() || list == null)
            return null;
        else {
            List<HccHead> listhcc = new ArrayList<>();

            list.forEach((Object[] x) -> {
                HccHead hh = new HccHead();
                Product p = new Product();
                hh.setId(((BigInteger) x[0]).longValue());
                hh.setHcchBatch((String) x[1]);
                p.setNameProduct((String) x[2]);
                p.setTypeProduct(ProductType.valueOf((String) x[3]));
                hh.setProduct(p);
                Instant instant = Instant.ofEpochMilli(((Date) x[4]).getTime());
                hh.setDateCreate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
                hh.setAnalysis((String) x[5]);
                hh.setSapCode((String) x[6]);
                hh.setPeriodicity((String) x[7]);
                listhcc.add(hh);
            });
            return listhcc;
        }
    }

    @Transactional
    @Override
    public HccdResultadoDto registrarConImagen(String json, byte[] file, String nombreArchivo, String tipo) {
        try {
            HccdResultadoDto resultado = new HccdResultadoDto();
            HccHead dto = JSON_MAPPER.readValue(json, HccHead.class);
            if (dto != null) {
                UserImptek ui = this.obtenerUsuarioSesion();
                dto.setUserName(ui.getEmployee().getCompleteName());
                dto.setJob(ui.getEmployee().getJob());
                dto.setWorkArea(ui.getEmployee().getArea().getNameArea());
                LOG.info("> objeto a guardar: " + dto.toString());
                dto.setDateCreate(LocalDate.now());
                this.repo.save(dto);
                if (file.length > 0) {
                    try {
                        final String path = crearPathArchivo(dto, nombreArchivo);
                        PassFileToRepository.saveLocalFile(path, file);
                        dto.setRutaImagenMuestra(path);
                        LOG.info(String.format("HCC -> %s :Archivo guardado %s", dto.getId(), path));
                    } catch (IOException e) {
                        LOG.error(String.format("Error al subir Documento %s", e.getMessage()));
                        throw new HCCErrorException("Error al subir el archivo");
                    }
                }
                this.repo.save(dto);
                this.generarReporte(dto, resultado);
                return resultado;
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    private UserImptek obtenerUsuarioSesion() {
        String nombreUsuario = UtilidadesSeguridad.nombreUsuarioEnSesion();
        Optional<UserImptek> usuario = this.repoUsuario.findByIdUser(nombreUsuario);
        if (!usuario.isPresent())
            throw new UsuarioErrorException("Usuario en sesión no encontrado");
        return usuario.get();
    }

    private String crearPathArchivo(HccHead hccHead, String nombreArchivo) {
        String path = crearRutaAlmacenamiento(hccHead.getId()).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(hccHead.getId()).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);
        LOG.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
        return path;
    }

    private String crearRutaAlmacenamiento(long productoId) {
        try {
            String carpeta = configuracion.getRutaBase().concat(File.separator).concat("HCCS")
                    .concat(File.separator).concat(String.valueOf(productoId));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectories(path);
            return carpeta;
        } catch (IOException e) {
            LOG.error(String.format("Error al subir Imagen %s", e.getMessage()));
            throw new SolicitudEnsayoErrorException("Error al crear el directorio");
        }
    }

    private void generarReporte(HccHead hh, HccdResultadoDto resultado) {
        ReportDto rpt = new ReportDto();
        rpt.setHccHead(hh);
        ReportHeadT rHTemp = new ReportHeadT();
        rHTemp.setType(hh.getReportHeadT());
        rpt.setRh(this.serviceRH.findHeadByTypeReport(rHTemp));

        List<ProductType> tipos = Arrays.asList(ProductType.PRODUCTO_TERMINADO, ProductType.PRODUCTO_MAQUILA, ProductType.PRODUCTO_EN_PROCESO);
        if (tipos.contains(hh.getProduct().getTypeProduct())) {
            String statusReport = GenerateReportQuality.runReportJasperHcc(rpt);
            if (statusReport.equals(GenerateReportQuality.REPORT_SUCCESS)) {
                LOG.info(">> Reporte generado correctamente");
                resultado.setMensaje("El reporte de la HCC " + hh.getSapCode()
                        + "ha sido creado satisfactoriamente");
            } else {
                LOG.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                throw new HCCErrorException("El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
            }
        } else {
            String pathFile = GenerateReportQuality.runReportJasperHcc(rpt);
            resultado.setRutaArchivo(pathFile);
            if (UtilidadesCadena.noEsNuloNiBlanco(resultado.getRutaArchivo())) {
                LOG.info(">> Reporte generado correctamente");
                resultado.setMensaje("El reporte de la HCC " + hh.getSapCode() + "ha sido creado satisfactoriamente");
            } else {
                LOG.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                throw new HCCErrorException("El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
            }
        }
    }

}
