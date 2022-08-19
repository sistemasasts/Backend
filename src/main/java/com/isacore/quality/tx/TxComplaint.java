package com.isacore.quality.tx;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.EmailDto;
import com.isacore.quality.model.Complaint;
import com.isacore.quality.model.Problem;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.service.IComplaintService;
import com.isacore.quality.service.IProblemService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;

import com.isacore.util.PassFileToRepository;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxComplaint {
	public static final String TX_NAME_SaveComplaint = "SaveComplaint";
	public static final String TX_CODE_SaveComplaint = "TxQQRsaveComplaint";

	public static final String TX_NAME_GetAllComplaint = "GetAllComplaint";
	public static final String TX_CODE_GetAllComplaint = "TxQQRgetAllComplaint";

	public static final String TX_NAME_GenerateReportComplaint = "GenerateReportComplaint";
	public static final String TX_CODE_GenerateReportComplaint = "TxQQRgenerateReportComplaint";

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IComplaintService complaintService;

	@Autowired
	private IProblemService problemService;

	@Autowired
	private IUserImptekService serviceUI;

	/**
	 * TX NAME: SaveComplaint guarda los reclamos de materia prima.
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRSaveComplaint(WebRequestIsa wri) {
		logger.info("> TX: TxQQRSave/SetComplaint");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SaveComplaint);
		wrei.setTransactionCode(TX_CODE_SaveComplaint);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Complaint.class);
				Complaint complaint = JSON_MAPPER.readValue(jsonValue, Complaint.class);
				UserImptek ui = this.serviceUI.findOnlyUserByNickname(complaint.getAsUser());
				complaint.setUserName(ui.getEmployee().getCompleteName());
				complaint.setJob(ui.getEmployee().getJob());
				complaint.setWorkArea(ui.getEmployee().getArea().getNameArea());
				logger.info("> objeto a guardar: " + complaint.toString());
				complaint.setDateCreateComplaint(LocalDateTime.now());
				for (Problem p : complaint.getListProblems()) {

					if(p.getPictureStringB64()!= null) {
						if ( p.getPictureStringB64().contains("base64")) {
							String uri = PassFileToRepository.base64ToFile(p.getPictureStringB64(), p.getNameFileP(),
									p.getExtensionFileP());
							if (uri != "") {
								p.setPictureStringB64(uri);
							} else {
								p.setExtensionFileP(null);
								p.setNameFileP(null);
								p.setPictureStringB64(null);
							}
						}
					}
					

				}
				
				logger.info("> paso el for de imágenes");
				Complaint cc = this.complaintService.create(complaint);

				if (cc != null) {
					logger.info(">> Reclamo de MP guardado correctamente");
					Complaint ccTmp = this.complaintService.findById(cc);
					wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					String json = JSON_MAPPER.writeValueAsString(ccTmp);

					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				} else {
					logger.error(">> Error al guardar el reclamo de MP");
					wrei.setMessage("Error al guardar el reclamo de MP");
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Complaint.class);
				e.printStackTrace();
				wrei.setMessage(WebResponseMessage.ERROR_TO_CLASS);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: GetComplaint recupera los reclamos de materia prima.
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetAllComplaint(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetAllComplaint");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllComplaint);
		wrei.setTransactionCode(TX_CODE_GetAllComplaint);

		List<Complaint> complaints = this.complaintService.findAll();

		if (complaints.isEmpty() || complaints == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else {
			try {
				// Serializamos la lista a JSON
				String json = JSON_MAPPER.writeValueAsString(complaints);

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

	/**
	 * TX NAME: Generate Report Reclamos materia prima.
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRGenerateReportComplaint(WebRequestIsa wri) {
		logger.info("> TX: TxQQRGenerateReportComplaint");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GenerateReportComplaint);
		wrei.setTransactionCode(TX_CODE_GenerateReportComplaint);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Complaint.class);
				Complaint complaint = JSON_MAPPER.readValue(jsonValue, Complaint.class);

				Complaint cc = this.complaintService.findById(complaint);
				if (cc != null) {
					logger.info(">> Reclamo de MP obtenido correctamente");
					// this.problemService.dataTratamientImagesReport(cc.getListProblems());
					String fileReport = GenerateReportQuality.runGenerateReportComplaint(cc);

					if (!fileReport.equals(GenerateReportQuality.REPORT_ERROR)) {
						logger.info(">> Reporte generado correctamente");
						EmailDto emd = new EmailDto();
						emd.setFilePath(fileReport);
						String jsonR = JSON_MAPPER.writeValueAsString(emd);

						wrei.setParameters(jsonR);
						wrei.setMessage(
								"Reclamo de MP::" + cc.getProduct().getNameProduct() + "::: creado satisfactoriamente");
						wrei.setStatus(WebResponseMessage.STATUS_OK);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);
					} else {
						logger.info(">> No se ha podido generar el reporte");
						wrei.setMessage(
								"No se pudo Generar el certificado de calidad::" + cc.getProduct().getNameProduct());
						wrei.setStatus(WebResponseMessage.STATUS_ERROR);
						return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					logger.error(">> Error al consultar Reclamo de MP");
					wrei.setMessage("Error al consultar Reclamo de MP");
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Complaint.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_CLASS);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}
		}

	}

}
