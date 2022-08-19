package com.isacore.security.tx;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxSecurity {

	public static final String TX_NAME_AAS = "AAS";
	public static final String TX_CODE_AAS = "TxAAS";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Autowired
	private IUserImptekService userImptekService;

	public ResponseEntity<Object> AAS(WebRequestIsa wri) {

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_AAS);
		wrei.setTransactionCode(TX_CODE_AAS);

		logger.info("> TX: TxSecurityAAS");
		String jsonValue = wri.getParameters();//Crypto.decrypt();

		
			try {
				logger.info("> mapeando json a la clase: " + LoginIsa.class);
				ObjectMapper mapper = new ObjectMapper();
				LoginIsa li = mapper.readValue(jsonValue, LoginIsa.class);
				logger.info("> Login ISA::: " + li.getUserName());
				UserImptek ui = this.userImptekService.findByUserImptek(li.getUserName());

				if (ui == null) {
					logger.info("> UserImptek not found");
					wrei.setMessage("Usuario no encontrado");
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					if (ui.getUserPass().equals(li.getPass())) {
						String json = JSON_MAPPER.writeValueAsString(ui);
						//String jsonCryp = Crypto.encrypt(json);
							wrei.setMessage(WebResponseMessage.SEARCHING_OK);
							wrei.setParameters(json);
							wrei.setStatus(WebResponseMessage.STATUS_OK);
							return new ResponseEntity<Object>(wrei, HttpStatus.OK);						
					}else {
						wrei.setMessage("Clave de acceso Incorrecta");
						wrei.setStatus(WebResponseMessage.STATUS_ERROR);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);
					}
				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + LoginIsa.class);
				e.printStackTrace();
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

	}

}
