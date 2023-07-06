package com.isacore.quality.tx;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.isacore.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.EmailDto;
import com.isacore.quality.dto.ReportDto;
import com.isacore.quality.model.HccDetail;
import com.isacore.quality.model.HccHead;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.model.Property;
import com.isacore.quality.model.ReportHeadT;
import com.isacore.quality.model.Test;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.service.IFeatureService;
import com.isacore.quality.service.IHccHeadService;
import com.isacore.quality.service.IProductService;
import com.isacore.quality.service.IPropertyService;
import com.isacore.quality.service.IProviderService;
import com.isacore.quality.service.IReportHeadTService;
import com.isacore.quality.service.ITestService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;

import javassist.expr.NewArray;

@Component
public class TxHcc {

    public static final String TX_NAME_GenerateHcc = "GenerateHcc";
    public static final String TX_CODE_GenerateHcc = "TxQQRgenerateHCC";

    public static final String TX_NAME_SetHcc = "Create/UpdateHcc";
    public static final String TX_CODE_SetHcc = "TxQQRsetHCC";

    public static final String TX_NAME_GetAllHCCTP = "GetAllHCC_TP";
    public static final String TX_CODE_GetAllHCCTP = "TxQQRgetHCCTP";

    public static final String TX_NAME_GetPropertyByIdProdutandIdPropertyList = "GetPropertyByIdProdutandIdPropertyList";
    public static final String TX_CODE_GetPropertyByIdProdutandIdPropertyList = "TxQQRgetPropertyByIdProdutandIdPropertyList";

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IReportHeadTService serviceRH;

    @Autowired
    private IProductService serviceProducto;

    @Autowired
    private IHccHeadService serviceHccH;

    @Autowired
    private ITestService serviceTest;

    @Autowired
    private IUserImptekService serviceUI;

    @Autowired
    private IFeatureService serviceFeature;

    @Autowired
    IProviderService serviceProvider;

    @Autowired
    IPropertyService serviceProperty;

    /**
     * TX NAME: GetAllHCC_TP obtiene las hcc en base al tipo de producto TP
     *
     * @param wri
     * @return
     */
    public ResponseEntity<Object> TxQQRgetHCCPT(WebRequestIsa wri) {
        logger.info("> TX: TxQQRgetHCCPT");

        WebResponseIsa wrei = new WebResponseIsa();
        wrei.setTransactionName(TX_NAME_GetAllHCCTP);
        wrei.setTransactionCode(TX_CODE_GetAllHCCTP);

        if (wri.getParameters() == null || wri.getParameters().isEmpty()) {

            try {

                logger.info("> Obtiene todas las HCC");
                List<HccHead> listHcc = this.serviceHccH.findOnlyHccHead();

                if (listHcc == null) {
                    logger.info("> No existen Hcc");
                    wrei.setStatus(WebResponseMessage.STATUS_INFO);
                    wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
                    return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
                } else {
                    String json = JSON_MAPPER.writeValueAsString(listHcc);


                    wrei.setMessage(WebResponseMessage.SEARCHING_OK);
                    wrei.setStatus(WebResponseMessage.STATUS_OK);
                    wrei.setParameters(json);
                    return new ResponseEntity<Object>(wrei, HttpStatus.OK);

                }

            } catch (IOException e) {
                logger.error("> No se ha podido serializar el JSON a la clase: " + HccHead.class);
                e.printStackTrace();
                wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
                return new ResponseEntity<Object>(wri, HttpStatus.BAD_REQUEST);
            }

        } else {
            String jsonValue = wri.getParameters();

            try {
                logger.info("> Tipo de producto::::: " + jsonValue);
                List<HccHead> listHcc = this.serviceHccH.findOnlyHccHead(jsonValue);

                if (listHcc == null) {
                    logger.info("> No existen Hcc");
                    wrei.setStatus(WebResponseMessage.STATUS_INFO);
                    wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
                    return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
                } else {
                    String json = JSON_MAPPER.writeValueAsString(listHcc);

                    wrei.setMessage(WebResponseMessage.SEARCHING_OK);
                    wrei.setStatus(WebResponseMessage.STATUS_OK);
                    wrei.setParameters(json);
                    return new ResponseEntity<Object>(wrei, HttpStatus.OK);

                }
            } catch (IOException e) {
                logger.error("> No se ha podido serializar el JSON a la clase: " + HccHead.class);
                e.printStackTrace();
                wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
                return new ResponseEntity<Object>(wri, HttpStatus.BAD_REQUEST);
            }

        }
    }

    /**
     * Transacción Para generar la estructura de la HCC, vincular los datos de los
     * Test por el número de lote y código de la propiedad.
     *
     * @return
     */
    public ResponseEntity<Object> TxQQRgenerateHCC(WebRequestIsa wri) {
        logger.info("> TX: TxQQRgenerateHCC");

        WebResponseIsa wrei = new WebResponseIsa();
        wrei.setTransactionName(TX_NAME_GenerateHcc);
        wrei.setTransactionCode(TX_CODE_GenerateHcc);

        if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
            logger.info("> Objeto vacío");
            wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
            wrei.setStatus(WebResponseMessage.STATUS_INFO);
            return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
        } else {
            String jsonValue = wri.getParameters();

            try {
                logger.info("> mapeando json a la clase: " + HccHead.class);
                HccHead hh = JSON_MAPPER.readValue(jsonValue, HccHead.class);
                HccHead hhg = getHccHead(hh);
                if (hhg != null) {
                    String json = JSON_MAPPER.writeValueAsString(hhg);


                    wrei.setMessage(WebResponseMessage.SEARCHING_OK);
                    wrei.setStatus(WebResponseMessage.STATUS_OK);
                    wrei.setParameters(json);
                    return new ResponseEntity<Object>(wrei, HttpStatus.OK);


                } else {
                    logger.error("> Error al generar la HCC: " + HccHead.class);
                    wrei.setMessage(WebResponseMessage.ERROR_GENERATE_HCC);
                    wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                    return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (IOException e) {
                logger.error("> No se ha podido serializar el JSON a la clase: " + HccHead.class);
                wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
                wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
            }
        }

    }

    public ResponseEntity<Object> TxQQRSetHCC(WebRequestIsa wri) {
        logger.info("> TX: TxQQRSetHCC");

        WebResponseIsa wrei = new WebResponseIsa();
        wrei.setTransactionName(TX_NAME_SetHcc);
        wrei.setTransactionCode(TX_CODE_SetHcc);

        if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
            logger.info("> Objeto vacío");
            wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
            wrei.setStatus(WebResponseMessage.STATUS_INFO);
            return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
        } else {

            String jsonValue = wri.getParameters();

            try {
                logger.info("> mapeando json a la clase: " + HccHead.class);
                HccHead hh = JSON_MAPPER.readValue(jsonValue, HccHead.class);
                UserImptek ui = this.serviceUI.findOnlyUserByNickname(hh.getAsUser());
                hh.setUserName(ui.getEmployee().getCompleteName());
                hh.setJob(ui.getEmployee().getJob());
                hh.setWorkArea(ui.getEmployee().getArea().getNameArea());
                logger.info("> objeto a guardar: " + hh.toString());
                hh.setDateCreate(LocalDate.now());

                HccHead hcc = this.serviceHccH.create(hh);
                if (hcc != null) {
                    logger.info(">> Hcc guardada correctamente");
                    wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
                    wrei.setStatus(WebResponseMessage.STATUS_OK);

                    ReportDto rpt = new ReportDto();
                    rpt.setHccHead(hh);
                    ReportHeadT rHTemp = new ReportHeadT();
                    rHTemp.setType(hcc.getReportHeadT());
                    rpt.setRh(this.serviceRH.findHeadByTypeReport(rHTemp));

                    List<ProductType> tipos = Arrays.asList(ProductType.PRODUCTO_TERMINADO, ProductType.PRODUCTO_MAQUILA, ProductType.PRODUCTO_EN_PROCESO);
                    if (tipos.contains(hcc.getProduct().getTypeProduct())) {
                        String statusReport = GenerateReportQuality.runReportJasperHcc(rpt);
                        if (statusReport.equals(GenerateReportQuality.REPORT_SUCCESS)) {
                            logger.info(">> Reporte generado correctamente");
                            wrei.setMessage("El reporte de la HCC " + hh.getSapCode()
                                    + "ha sido creado satisfactoriamente");
                            wrei.setStatus(WebResponseMessage.STATUS_OK);
                            return new ResponseEntity<Object>(wrei, HttpStatus.OK);
                        } else {
                            logger.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                            wrei.setMessage("No se ha podido generar el reporte");
                            wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                            return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        String pathFile = GenerateReportQuality.runReportJasperHcc(rpt);
                        EmailDto emd = new EmailDto();
                        emd.setFilePath(pathFile);
                        if (!emd.getFilePath().isEmpty()) {
                            logger.info(">> Reporte generado correctamente");
                            wrei.setMessage("El reporte de la HCC " + hh.getSapCode()
                                    + "ha sido creado satisfactoriamente");
                            String json = JSON_MAPPER.writeValueAsString(emd);

                            wrei.setParameters(json);
                            wrei.setStatus(WebResponseMessage.STATUS_OK);
                            return new ResponseEntity<Object>(wrei, HttpStatus.OK);
                        } else {
                            logger.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                            wrei.setMessage("No se ha podido generar el reporte");
                            wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                            return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }

                } else {
                    logger.error(">> Error al guardar la Hcc");
                    wrei.setMessage("Error al guardar la HCC");
                    wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                    return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (IOException e) {
                logger.error("> No se ha podido serializar el JSON a la clase: " + HccHead.class);
                wrei.setMessage(WebResponseMessage.ERROR_TO_CLASS);
                wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
            }
        }

    }

    /**
     * Transacción Para obtener la propiedad de acuerdo al idPropiedad, idProducto
     *
     * @return
     */

    public ResponseEntity<Object> TxQQRGetPropertyByIdProdutandIdPropertyList(WebRequestIsa wri) {
        logger.info("> TX: TxQQRGetPropertyByIdProdutandIdPropertyList");

        WebResponseIsa wrei = new WebResponseIsa();
        wrei.setTransactionName(TX_NAME_GetPropertyByIdProdutandIdPropertyList);
        wrei.setTransactionCode(TX_CODE_GetPropertyByIdProdutandIdPropertyList);

        if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
            logger.info("> Objeto vacío");
            wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
            wrei.setStatus(WebResponseMessage.STATUS_INFO);
            return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
        } else {
            String jsonValue = wri.getParameters();

            try {
                logger.info("> mapeando json a la clase: " + Property.class);
                Property pp = JSON_MAPPER.readValue(jsonValue, Property.class);
                Property pTMP = this.serviceProperty.findByIdProductandIdProperty(pp.getProduct(), pp.getPropertyList());
                if (pTMP != null) {
                    Property aux = new Property();
                    aux.setMinProperty(pTMP.getMinProperty());

                    String json = JSON_MAPPER.writeValueAsString(pTMP);


                    wrei.setMessage(WebResponseMessage.SEARCHING_OK);
                    wrei.setStatus(WebResponseMessage.STATUS_OK);
                    wrei.setParameters(json);
                    return new ResponseEntity<Object>(wrei, HttpStatus.OK);

                } else {
                    logger.error("> Error al encontrar la Propiedad: " + Property.class);
                    wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
                    wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                    return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } catch (IOException e) {
                e.printStackTrace();
                logger.error("> No se ha podido serializar el JSON a la clase: " + Property.class);
                wrei.setMessage(WebResponseMessage.ERROR_TO_CLASS);
                wrei.setStatus(WebResponseMessage.STATUS_ERROR);
                return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
            }

        }
    }

    private HccHead getHccHead(HccHead hh) {
        logger.info(">>> mthod: getHccHead::::");
        Product p = this.serviceProducto.findOnlyProductById(hh.getProduct());
        if (p != null) {

            List<ProductType> tipos = Arrays.asList(ProductType.PRODUCTO_TERMINADO, ProductType.PRODUCTO_MAQUILA, ProductType.PRODUCTO_EN_PROCESO);
            if (tipos.contains(p.getTypeProduct())) {

                ReportHeadT rep = new ReportHeadT();
                rep.setType("HCCPT");

                ReportHeadT rh = this.serviceRH.findHeadByTypeReport(rep);

                if (rh != null) {
                    logger.info(">>> mthod: getHccHead::::PT::::" + p.getIdProduct() + ":::" + p.getNameProduct());
                    HccHead hhg = new HccHead();
                    hhg.setReportHeadT("HCCPT");
                    hhg.setProduct(p);
                    hhg.setHccNorm("");
                    hhg.setCode(rh.getCode());
                    hhg.setReview(p.getReview());
                    hhg.setReference(p.getItcdq());
                    hhg.setOf("");
                    hhg.setReferralGuide("");
                    hhg.setHcchBatch(hh.getHcchBatch());
                    hhg.setPeriodicity(hh.getPeriodicity());
                    hhg = gerenateDetailOfHcc(hhg, hh.getProduct(), hh.getPeriodicity());
                    this.agregarImagenPatron(hhg);
                    mergeHccTests(hhg, hh.getHcchBatch());
                    return hhg;
                } else {
                    logger.info(">>> mthod: getHccHead::::no se ha el reportHeaderTemplate HCCPT");
                    return null;
                }
            } else {
                ReportHeadT rep = new ReportHeadT();
                rep.setType("HCCMP");

                ReportHeadT rh = this.serviceRH.findHeadByTypeReport(rep);

                if (rh != null) {
                    p.setProviders(this.serviceProvider.findByProductId(p.getIdProduct()));
                    logger.info(">>> mthod: getHccHead::::MP::::" + p.getIdProduct() + ":::" + p.getNameProduct());
                    HccHead hhg = new HccHead();
                    hhg.setReportHeadT("HCCMP");
                    hhg.setProduct(p);
                    hhg.setCode(rh.getCode());
                    hhg.setReview(p.getReview());
                    hhg.setReferralGuide("");
                    hhg.setHcchBatch(hh.getHcchBatch());
                    hhg = gerenateDetailOfHcc(hhg, hh.getProduct());
                    mergeHccTests(hhg, hh.getHcchBatch());
                    return hhg;
                } else {
                    logger.info(">>> mthod: getHccHead::::no se ha el reportHeaderTemplate HCCMP");
                    return null;
                }
            }
        } else {
            logger.info(">>> mthod: getHccHead::::no se ha encontrado el producto con id "
                    + hh.getProduct().getIdProduct());
            return null;
        }

    }

    private HccHead gerenateDetailOfHcc(HccHead hh, Product p, String... period) {

        logger.info(">>>> mthod: gerenateDetailOfHcc::::");
        List<HccDetail> detail = new ArrayList<>();
        System.out.println("tamaño array::::::: " + period.length);
        logger.info(">>> variable::::" + hh.toString());
        Product pp;

        if (period.length == 0 || period == null)
            pp = this.serviceProducto.findById(hh.getProduct());
        else
            pp = this.serviceProducto.findProductByIdAndPeriod(p.getIdProduct(), period[0]);

        if (pp != null) {
            for (Property prop : pp.getProperties()) {
                HccDetail hd = new HccDetail();
                // hd.setNameNorm(prop.getPropertyList().getNormName());
                hd.setNameNorm(prop.getPropertyNorm());
                hd.setIdProperty(prop.getPropertyList().getIdProperty());
                // hd.setIdProperty(prop.getIdProperty());
                hd.setNameProperty(prop.getPropertyList().getNameProperty());
                // hd.setNameProperty(prop.getNameProperty());
                hd.setTypeProperty(prop.getTypeProperty());
                hd.setUnit(prop.getUnitProperty());
                hd.setMin(prop.getMinProperty());
                hd.setMax(prop.getMaxProperty());
                hd.setView(prop.getViewProperty());
                // hd.setViewOnHcc(prop.isViewPropertyOnHcc());
                hd.generateSpecifications();
                detail.add(hd);
            }
        }


        hh.setDetail(detail);
        System.out.println(hh);
        return hh;
    }

    private void mergeHccTests(HccHead hh, String batch) {
        logger.info(">>>>> mthod: mergeHccTests::::");
        List<Test> tests = this.serviceTest.findByBatch(batch);
        List<Test> testsTMP = this.serviceTest.findByBatchAll(batch);
        List<Test> testsFail = new ArrayList<>();
        for (Test tm : testsTMP) {
            if (tm.getPrommissing() == false) {
                testsFail.add(tm);
            }
        }


        hh.getDetail().forEach(x -> x.setPassTest(false));

        if (tests == null || tests.isEmpty()) {
            logger.warn(">>>>> No se encontraron pruebas del lote:::: " + batch);
        } else {
            for (Test t : tests) {
                for (HccDetail hd : hh.getDetail()) {
                    if (t.getIdProperty().equals(hd.getIdProperty())) {
                        hd.setResult(t.getResultTest());
                        hd.evaluateResult();
                        break;
                    }
                }
            }
            if (hh.getProduct().getTypeProduct().equals(ProductType.MATERIA_PRIMA)) {
                hh.setIdProviderMP(testsTMP.get(0).getIdProvider());
                hh.setDateOrder(testsTMP.get(0).getDateLog().toLocalDate());
            }
        }

        if (testsFail == null || testsFail.isEmpty()) {
            logger.info(">>>>> mthod: mergeHccTests Fail::::");
        } else {
            for (Test tf : testsFail) {
                for (HccDetail hd : hh.getDetail()) {
                    if (hd.getResult() == null) {
                        if (tf.getIdProperty().equals(hd.getIdProperty())) {
                            hd.setResult(tf.getResultTest());
                            hd.evaluateResult();
                            break;
                        }
                    }
                }
            }
            if (hh.getProduct().getTypeProduct().equals(ProductType.MATERIA_PRIMA)) {
                if (hh.getIdProviderMP() == null) {
                    hh.setIdProviderMP(testsFail.get(0).getIdProvider());
                    hh.setDateOrder(testsFail.get(0).getDateLog().toLocalDate());
                }

            }
        }
    }

    private void agregarImagenPatron(HccHead hcc) {
        if (UtilidadesCadena.noEsNuloNiBlanco(hcc.getProduct().getImagenPatronRuta())) {
            byte[] archivo = new byte[0];
            try {
                archivo = PassFileToRepository.readLocalFile(hcc.getProduct().getImagenPatronRuta());
                if (archivo.length > 0) {
                    String imagen = String.format("data:%s;base64,%s", hcc.getProduct().getImagenPatronTipo(),
                            Base64.getEncoder().encodeToString(archivo));
                    hcc.setImagenPatron64(imagen);
                }
            } catch (IOException e) {
                logger.info("no se pudo obtener la imagen patron");
            }
        }
    }
}
