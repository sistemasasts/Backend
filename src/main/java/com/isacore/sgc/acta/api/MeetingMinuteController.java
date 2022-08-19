package com.isacore.sgc.acta.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.security.tx.LoginIsa;
import com.isacore.security.tx.TxSecurity;
import com.isacore.sgc.acta.model.MeetingMinute;
import com.isacore.sgc.acta.service.impl.MeetingMinuteServiceImpl;
import com.isacore.util.WebRequestIsa;

@RestController
@RequestMapping(value = "/minute")
public class MeetingMinuteController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeetingMinuteServiceImpl minuteService;
	
	@Autowired
	private TxSecurity txSecurity;

	@RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MeetingMinute>> findAll() {

		List<MeetingMinute> minutes = this.minuteService.findAll();

		this.logger.info("> obtiene la lista de actas");

		if (minutes == null || minutes.isEmpty())
			return new ResponseEntity<List<MeetingMinute>>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<List<MeetingMinute>>(minutes, HttpStatus.OK);
	}	

	@RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> meetingMinuteApi(@RequestBody WebRequestIsa wri) {
		
		switch (wri.getTransactionCode()) {		
			
		case "TxSecurityAAS":			
			return this.txSecurity.AAS(wri);

		default:
			logger.info("> La transacción solicitada no existe: TX-> " + wri.getTransactionCode() + " " + wri.getTransactionName() + LoginIsa.class);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/utilCoding", method = RequestMethod.POST)
	public @ResponseBody String coding(@RequestBody String s) {
		
		logger.info("> String a encryptar" + s);
		
		return s;
	}
	
	@RequestMapping(value = "/utilDeCoding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String decoding(@RequestBody String s) {
		
		logger.info("> String a encryptar " + s);
		
		return s;
	}

}
