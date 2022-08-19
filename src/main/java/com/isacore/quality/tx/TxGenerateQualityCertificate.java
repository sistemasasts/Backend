package com.isacore.quality.tx;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.EmailDto;
import com.isacore.quality.dto.ReportDto;
import com.isacore.quality.model.ClientImptek;
import com.isacore.quality.model.HccHead;
import com.isacore.quality.model.NormProduct;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.QualityCertificate;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.service.IClientImptekService;
import com.isacore.quality.service.IHccHeadService;
import com.isacore.quality.service.IQualityCertificateService;

import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxGenerateQualityCertificate {

	public static final String Tx_NAME_GenerateQualityCertificate = "GenerateQualityCertificate";
	public static final String Tx_CODE_GenerateQualityCertificate = "TxGQC";

	public static final String TX_NAME_GetAllClients = "GetAllClients";
	public static final String TX_CODE_GetAllClients = "TxQQRGAC";

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IQualityCertificateService service;

	@Autowired
	private IClientImptekService clientService;

	@Autowired
	private IHccHeadService hccHeadService;

	/**
	 * TX NAME: GenerateQualityCertificate genera un certificado de una hcc en
	 * formato PDF
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxGQC(WebRequestIsa wri) {
		logger.info("> TX: TxGQC");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(Tx_NAME_GenerateQualityCertificate);
		wrei.setTransactionCode(Tx_CODE_GenerateQualityCertificate);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_ERROR);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + QualityCertificate.class);
				QualityCertificate qc = JSON_MAPPER.readValue(jsonValue, QualityCertificate.class);
				logger.info("> objeto a guardar: " + qc.toString());
				if (qc.getClientImptek().getIdClient() == null) {
					this.clientService.create(qc.getClientImptek());
				}
				int count = this.service.findCertificateByPK(qc.getHccHead().getId(),
						qc.getClientImptek().getIdClient());
				qc.setCountCertificate(count);
				if (count == 1)
					this.service.createCertificate(qc);
				else
					this.service.updateCount(count, qc.getHccHead().getId(), qc.getClientImptek().getIdClient(),
							qc.getClientPrint());

				String json = JSON_MAPPER.writeValueAsString(qc);

				HccHead hccTmp = this.hccHeadService.findById(qc.getHccHead());
				ReportDto qcD = new ReportDto();
				qcD.setHccHead(hccTmp);
				if (hccTmp.getProduct().getNorms().isEmpty()) {
					qcD.setNormProductText("-");
				} else {
					List<NormProduct> listNorms = hccTmp.getProduct().getNorms();
					String normTextTmp = "-";
					for (NormProduct norm : listNorms) {
						if (norm.getState().equalsIgnoreCase("vigente"))
							normTextTmp = norm.getNorm().getNameNorm();
					}
					qcD.setNormProductText(normTextTmp);
				}
				qcD.setQc(qc);
				String pathFile = GenerateReportQuality.runReportJasperQualityCertificate(qcD);

				if (!pathFile.equals(GenerateReportQuality.REPORT_ERROR)) {
					logger.info(">> Reporte generado correctamente");
					EmailDto emd = new EmailDto();
					emd.setFilePath(pathFile);
					String jsonR = JSON_MAPPER.writeValueAsString(emd);

					wrei.setParameters(jsonR);
					wrei.setMessage("Certificado de calidad::" + qc.getHccHead().getSapCode()
							+ "::: creado satisfactoriamente");
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);
				} else {
					logger.info(">> No se ha podido generar el reporte");
					wrei.setMessage("No se pudo Generar el certificado de calidad::" + qc.getHccHead().getSapCode());
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/*
	 * 
	 */

	public ResponseEntity<Object> TxQQRGAC(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProducts");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllClients);
		wrei.setTransactionCode(TX_CODE_GetAllClients);

		List<ClientImptek> clients = this.clientService.findAll();

		if (clients.isEmpty() || clients == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else
			try {
				// Serializamos la lista a JSON
				String json = JSON_MAPPER.writeValueAsString(clients);
				// encriptamos el JSON

				wrei.setMessage(WebResponseMessage.SEARCHING_OK);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				wrei.setParameters(json);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> error al serializar el JSON");
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
}
