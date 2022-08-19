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
import com.isacore.quality.model.Provider;
import com.isacore.quality.service.IProviderService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxProvider {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	public static final String TX_NAME_GetAllProviders = "GetAllProviders";
	public static final String TX_CODE_GetAllProviders = "TxQQRgetAllProviders";

	@Autowired
	private IProviderService providerService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: GetAllProviders obtiene todos los proveedores de la BBDD
	 * 
	 * @param wri
	 * @return
	 */

	public ResponseEntity<Object> TxQQRgetProviders(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetAllProviders");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllProviders);
		wrei.setTransactionCode(TX_CODE_GetAllProviders);

		List<Provider> providers = this.providerService.findAll();

		if (providers.isEmpty() || providers == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else {
			try {
				// Serializamos la lista a JSON
				String json = JSON_MAPPER.writeValueAsString(providers);

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
}
