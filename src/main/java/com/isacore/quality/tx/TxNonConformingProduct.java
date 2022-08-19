package com.isacore.quality.tx;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.model.ExitMaterialHistory;
import com.isacore.quality.model.NonconformingProduct;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.service.IExitMaterialHistoryService;
import com.isacore.quality.service.INonconformingProductService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxNonConformingProduct {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_GetAllNCP = "GetAllNCP";
	public static final String TX_CODE_GetAllNCP = "TxQQRgeAlltNCP";

	public static final String TX_NAME_GetNCPById = "GetNCPById";
	public static final String TX_CODE_GetNCPById = "TxQQRgetNCPId";

	public static final String TX_NAME_SetNCP = "SetNCP";
	public static final String TX_CODE_SetNCP = "TxQQRsetNCP";

	public static final String TX_NAME_CloseNCP = "Close NCP";
	public static final String TX_CODE_CloseNCP = "TxQQRcloseNCP";

	public static final String TX_NAME_SaveExitMaterial = "SaveExitMaterial";
	public static final String TX_CODE_SaveExitMaterial = "TxQQRsaveExitMaterial";

	@Autowired
	private INonconformingProductService service;

	@Autowired
	private IExitMaterialHistoryService exitMaterialService;

	@Autowired
	private IUserImptekService serviceUI;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: TxQQRgetAllNCP Devulve la lista completa de los productos no
	 * conforme almacenados en base de datos
	 * 
	 * @param wri parámetro de petición de respuesta.
	 * @return wrei respuesta del servidor con la lista de producto no conforme.
	 */
	public ResponseEntity<Object> TxQQRgetAllNCP(WebRequestIsa wri) {

		logger.info("> TX: TxQQRgeAlltNCP");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllNCP);
		wrei.setTransactionCode(TX_CODE_GetAllNCP);

		List<NonconformingProduct> ncps = this.service.findAll();

		if (ncps.isEmpty() || ncps == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else {
			ncps.forEach((NonconformingProduct x) -> {
				x.getProduct().setProperties(null);
				;
				x.getProduct().setFeature(null);
			});

			try {
				// Serializamos la lista a JSON
				String json = JSON_MAPPER.writeValueAsString(ncps);
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

	/**
	 * TX NAME: TxQQRgetNCPById Devulve un producto no conforme en base a su clave
	 * primaria
	 * 
	 * @param wri parámetro de petición de producto no conforme con su clave
	 *            primaria.
	 * @return wrei respuesta del servidor el registro de producto no conforme.
	 */
	public ResponseEntity<Object> TxQQRgetNCPById(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetNCPById");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetNCPById);
		wrei.setTransactionCode(TX_CODE_GetNCPById);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + NonconformingProduct.class);

				NonconformingProduct ncp = JSON_MAPPER.readValue(jsonValue, NonconformingProduct.class);
				logger.info("> id Nonconforming Product: " + ncp.getIdNCP());
				ncp = this.service.findById(ncp);

				if (ncp == null) {
					logger.info("> Nonconforming Product not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					String json = JSON_MAPPER.writeValueAsString(ncp);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + NonconformingProduct.class);
				e.printStackTrace();
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	public ResponseEntity<Object> TxQQRsetNCP(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsetProduct");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SetNCP);
		wrei.setTransactionCode(TX_CODE_SetNCP);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + NonconformingProduct.class);
				NonconformingProduct ncp = JSON_MAPPER.readValue(jsonValue, NonconformingProduct.class);
				logger.info("> objeto a guardar: " + ncp.toString());

				UserImptek ui = this.serviceUI.findOnlyUserByNickname(ncp.getAsUser());
				ncp.setDateUpdate(LocalDateTime.now());
				ncp.setUserName(ui.getEmployee().getCompleteName());
				ncp.setJob(ui.getEmployee().getJob());
				ncp.setWorkArea(ui.getEmployee().getArea().getNameArea());
				ncp.setState("Abierto");
				if(ncp.getIdNCP()== null) {
					ncp.setExistingMaterial(ncp.getAmountNonConforming());
				}				
				NonconformingProduct ncpp = this.service.create(ncp);
				ncpp.getProduct().setProperties(null);
				if (ncpp != null) {
					String json = JSON_MAPPER.writeValueAsString(ncpp);

					wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					wrei.setParameters(json);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				} else {
					logger.error(">> Error al guardar el PNC");
					wrei.setMessage("Error al guardar el PNC");
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + NonconformingProduct.class);
				wrei.setMessage("No se ha podido serializar la clase");
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: TxQQRcloseNCP cambia el estado del PNC a cerrado
	 * 
	 * @param wri parámetro de petición de producto no conforme con su clave
	 *            primaria.
	 * @return wrei respuesta del servidor con el mensaje de haber cerrado o no el
	 *         PNC correctamente.
	 */
	public ResponseEntity<Object> TxQQRcloseNCP(WebRequestIsa wri) {
		logger.info("> TX: TxQQRcloseNCP");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_CloseNCP);
		wrei.setTransactionCode(TX_CODE_CloseNCP);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + NonconformingProduct.class);
				ObjectMapper mapper = new ObjectMapper();
				NonconformingProduct ncp = mapper.readValue(jsonValue, NonconformingProduct.class);
				logger.info("> id Nonconforming Product: " + ncp.getIdNCP());
				ncp = this.service.findById(ncp);

				if (ncp == null) {
					logger.info("> Nonconforming Product not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					String statusReport = "";// GenerateReportQuality.runReportPentahoNCP(ncp.getIdNCP());
					if (statusReport.equals(GenerateReportQuality.REPORT_SUCCESS)) {
						logger.info(">> Reporte generado correctamente");
						wrei.setMessage("PNC::" + ncp.getIdNCP() + "::: cerrado correctamente");
						ncp.setState("Cerrado");

						UserImptek ui = this.serviceUI.findOnlyUserByNickname(ncp.getAsUser());

						ncp.setDateUpdate(LocalDateTime.now());
						ncp.setUserName(ui.getEmployee().getCompleteName());
						ncp.setJob(ui.getEmployee().getJob());
						ncp.setWorkArea(ui.getEmployee().getArea().getNameArea());
						this.service.update(ncp);
						wrei.setStatus(WebResponseMessage.STATUS_OK);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);
					} else {
						logger.info(">> No se ha podido generar el reporte");
						wrei.setMessage("No se pudo cerrar el PNC::" + ncp.getIdNCP());
						wrei.setStatus(WebResponseMessage.STATUS_ERROR);
						return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + NonconformingProduct.class);
				e.printStackTrace();
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: TxQQRregisterExitMaterial registra la salida de material PNC
	 * 
	 */
	public ResponseEntity<Object> TxQQRsaveExitMaterial(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsaveExitMaterial");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SaveExitMaterial);
		wrei.setTransactionCode(TX_CODE_SaveExitMaterial);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + ExitMaterialHistory.class);
				ExitMaterialHistory emh = JSON_MAPPER.readValue(jsonValue, ExitMaterialHistory.class);
				logger.info("> objeto a guardar: " + emh.toString());

				UserImptek ui = this.serviceUI.findOnlyUserByNickname(emh.getAsUser());
				emh.setDate(LocalDate.now());
				emh.setNameUser(ui.getEmployee().getCompleteName());
				emh.setJob(ui.getEmployee().getJob());
				emh.setWorkArea(ui.getEmployee().getArea().getNameArea());
				if (emh.getConcessionRequest() != null) {
					emh.getConcessionRequest().setDate(LocalDate.now());
					emh.getConcessionRequest().setQuantity(emh.getQuantity());
				}
				ExitMaterialHistory ncpp = this.exitMaterialService.create(emh);

				if (ncpp != null) {
					NonconformingProduct nAux = new NonconformingProduct();
					nAux.setIdNCP(ncpp.getNcpID());
					//nAux = this.service.consumeMaterialNC(ncpp.getNcpID(), ncpp.getQuantity());
					nAux.getProduct().setProperties(null);
					String json = JSON_MAPPER.writeValueAsString(nAux);

					wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					wrei.setParameters(json);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				} else {
					logger.error(">> Error al guardar la salida de material");
					wrei.setMessage("Error al guardar la salida de material");
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + ExitMaterialHistory.class);
				wrei.setMessage("No se ha podido serializar la clase");
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}
		}

	}

}
