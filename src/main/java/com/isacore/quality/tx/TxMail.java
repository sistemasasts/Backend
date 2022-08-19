package com.isacore.quality.tx;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.mail.Mail;
import com.isacore.quality.dto.EmailDto;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxMail {

	public static final String TX_NAME_SendEmail = "SendEmail";
	public static final String TX_CODE_SendEmail = "TxQQRsendEmail";

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Mail email;

	/**
	 * Transacción para enviar a correo electronico HCC
	 * 
	 * 
	 * @return
	 */
	public ResponseEntity<Object> TxQQRsendEmailHCC(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsendEmail");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SendEmail);
		wrei.setTransactionCode(TX_CODE_SendEmail);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + EmailDto.class);
				EmailDto em = JSON_MAPPER.readValue(jsonValue, EmailDto.class);
				if (em != null) {
					this.email.sendEmail(em.getContacts(), em.getFilePath(), em.getSubject(), em.getMessage());

					wrei.setMessage(WebResponseMessage.SEND_MESSAGE);
					wrei.setStatus(WebResponseMessage.STATUS_OK);

					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				} else {
					logger.error("> Error al  enviar el correo: " + EmailDto.class);
					wrei.setMessage(WebResponseMessage.ERROR_GENERATE_HCC);
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + EmailDto.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}
}
