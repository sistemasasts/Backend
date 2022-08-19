package com.isacore.sgc.acta.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.util.WebRequestIsa;

@RestController
@RequestMapping(value = "/quickresponse")
public class QuickResponseController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@RequestMapping(value = "/api", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RequestEntity<Object> txQuickResponse(@RequestBody WebRequestIsa wri){
		
		switch (wri.getTransactionCode()) {
		case "TxQRgetCommittees":
			
			
			break;

		default:
			break;
		}
		
		
		return null;
	}
	
}
