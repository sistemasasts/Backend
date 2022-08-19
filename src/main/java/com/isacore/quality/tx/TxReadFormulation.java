package com.isacore.quality.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.isacore.quality.read.formulation.GeneralReadFormulation;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxReadFormulation {

	public static final String TX_NAME_ReadFormulation = "ReadFormulation";
	public static final String TX_CODE_ReadFormulation = "TxQQRReadF";

	@Autowired
	private GeneralReadFormulation read;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<Object> TxQQRReadF(WebRequestIsa wri) {
		logger.info("> TX: " + TX_CODE_ReadFormulation);
		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_ReadFormulation);
		wrei.setTransactionCode(TX_CODE_ReadFormulation);

		try {
			read.run(wri.getUser());
			wrei.setMessage("Tarea ejecutada correctamente");
			wrei.setStatus(WebResponseMessage.STATUS_OK);
			return new ResponseEntity<Object>(wrei, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("> Error al leer el archivo");
			wrei.setMessage("Error al leer el archivo");
			wrei.setStatus(WebResponseMessage.STATUS_ERROR);
			return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
