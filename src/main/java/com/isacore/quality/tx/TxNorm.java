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
import com.isacore.quality.model.Norm;
import com.isacore.quality.service.INormService;

import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxNorm {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_GetAllNorms = "GetAllNorms";
	public static final String TX_CODE_GetAllNorms = "TxQQRgetNorms";

	public static final String TX_NAME_GetNormById = "GetNormById";
	public static final String TX_CODE_GetNormById = "TxQQRgetNormById";

	public static final String TX_NAME_GetByKindNorm = "GetByKindNorm";
	public static final String TX_CODE_GetByKindNorm = "TxQQRgetByKindNorm";

	@Autowired
	private INormService normService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: GetAllNorms, obtiene toda la lista de normas
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetNorms(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetNorms");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllNorms);
		wrei.setTransactionCode(TX_CODE_GetAllNorms);

		List<Norm> norms = this.normService.findAll();
		if (norms.isEmpty() || norms == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else {
			try {
				// encryptamos la lista a JSON
				String json = JSON_MAPPER.writeValueAsString(norms);
				wrei.setMessage(WebResponseMessage.SEARCHING_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
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
	 * TX NAME: GetNormById, obtiene una norma en base a su id
	 */
	public ResponseEntity<Object> TxQQRgetNormById(WebRequestIsa wri) {
		logger.info("> Tx TxQQRgetNormById");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetNormById);
		wrei.setTransactionCode(TX_CODE_GetNormById);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Norm.class);
				Norm n = JSON_MAPPER.readValue(jsonValue, Norm.class);
				logger.info("> id Norm: " + n.getIdNorm());
				n = this.normService.findById(n);

				if (n == null) {
					logger.info("> Norm not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					String json = JSON_MAPPER.writeValueAsString(n);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Norm.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: GetByKindNorm
	 */
	public ResponseEntity<Object> TxQQRgetByKindNorm(WebRequestIsa wri) {
		logger.info("> Tx TxQQRgetNormById");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetByKindNorm);
		wrei.setTransactionCode(TX_CODE_GetByKindNorm);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Norm.class);
				Norm n = JSON_MAPPER.readValue(jsonValue, Norm.class);
				logger.info("> kind Norm: " + n.getKind());
				List<Norm> norms = this.normService.findByKindNorm(n);

				if (norms.isEmpty() || norms == null) {
					logger.info("> Norms not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					String json = JSON_MAPPER.writeValueAsString(norms);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					wrei.setParameters(json);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Norm.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}
}
