package com.isacore.quality.classes.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.service.impl.AreaServiceImpl;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxRequestQualityUtil {

	public static final String TX_NAME_RNCP = "Request";
	public static final String TX_CODE_RNCP = "TxRNCP";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Autowired
	private RequestNCP rNCP;

	@Autowired
	private AreaServiceImpl areaService;

	public ResponseEntity<Object> requestNCP() {
		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_RNCP);
		wrei.setTransactionCode(TX_CODE_RNCP);

		logger.info("> TX: TxRNCP");
		rNCP.setAreas(this.areaService.findAll());

		try {

			String json = JSON_MAPPER.writeValueAsString(rNCP);

			wrei.setMessage(WebResponseMessage.SEARCHING_OK);
			wrei.setParameters(json);
			return new ResponseEntity<Object>(wrei, HttpStatus.OK);

		} catch (IOException e) {
			logger.error("> No se ha podido serializar la clase al JSON");
			e.printStackTrace();
			wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}
