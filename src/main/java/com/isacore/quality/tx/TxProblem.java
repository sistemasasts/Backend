package com.isacore.quality.tx;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.model.Problem;
import com.isacore.quality.model.Product;
import com.isacore.quality.service.IProblemService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxProblem {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_SaveProblem = "SaveProblem";
	public static final String TX_CODE_SaveProblem = "TxQQRsaveProblem";

	@Autowired
	private IProblemService problemService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: Crea y actualiza el maestro de problemas
	 * 
	 * @param wri es el objeto que trae los parámetros para la transacción
	 * @return Rertorna un mensaje de OK si la transacción fue exitosa y un BAD si
	 *         no lo fue
	 */

	public ResponseEntity<Object> TxQQRsetProblem(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsaveProblem");
		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SaveProblem);
		wrei.setTransactionCode(TX_CODE_SaveProblem);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Problem.class);
				Problem p = JSON_MAPPER.readValue(jsonValue, Problem.class);
				logger.info("> objeto a guardar: " + p.toString());
				Problem pp = this.problemService.create(p);

				String json = JSON_MAPPER.writeValueAsString(pp);

				wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

}
