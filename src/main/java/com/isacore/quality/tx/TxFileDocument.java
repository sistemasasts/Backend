package com.isacore.quality.tx;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.model.FileDocument;
import com.isacore.quality.service.IFileDocumentService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxFileDocument {
	
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_Download = "DownloadFile";
	public static final String TX_CODE_Download = "TxQQRDownloadFile";
	
	@Autowired
	private IFileDocumentService fileService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: Download Files devuelte el archivo del servidor en formato: base64
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRdownloadFiles(WebRequestIsa wri) {
		logger.info("> TX: TxQQRdownloadFiles");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_Download);
		wrei.setTransactionCode(TX_CODE_Download);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();
			
				try {
					logger.info("> mapeando json a la clase: " + FileDocument.class);
					FileDocument[] lFD =  JSON_MAPPER.readValue(jsonValue, FileDocument[].class);
					this.fileService.download(lFD);	
					String json = JSON_MAPPER.writeValueAsString(lFD);
					
						wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
						wrei.setParameters(json);
						wrei.setStatus(WebResponseMessage.STATUS_OK);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);
					
				} catch (IOException e) {
					logger.error("> No se ha podido serializar el JSON a la clase: " + FileDocument.class);
					e.printStackTrace();
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
					return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			
		}
	}

	

}
