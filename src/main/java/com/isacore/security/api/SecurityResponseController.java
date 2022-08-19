package com.isacore.security.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.security.tx.TxSecurity;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@RestController
@RequestMapping(value = "/security")
public class SecurityResponseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WebResponseIsa wrei;
	
	@Autowired
	private TxSecurity txSecurity;
	

	
	@Autowired
	private IUserImptekRepo userImptekRepo;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@RequestMapping(value = "/api", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> txQuickResponse(@RequestBody WebRequestIsa wri) {
		switch (wri.getTransactionCode()) {
		
		case TxSecurity.TX_CODE_AAS:
			return this.txSecurity.AAS(wri);
		
		default:
			logger.info("> La transacción solicitada no existe: TX-> " + wri.getTransactionCode());
			wrei.setTransactionName("Transacción no encontrada");
			wrei.setTransactionCode("TxNotFound");
			wrei.setStatus(WebResponseMessage.STATUS_ERROR);
			wrei.setMessage("Transacción no encontrada");
			return new ResponseEntity<Object>(wrei, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/seteoPassword", method = RequestMethod.GET)
	public void varios() {
		List<UserImptek> usuarios=this.userImptekRepo.findAll();
		usuarios.forEach(usuario ->{
			usuario.setUserPass(bcrypt.encode("123"));
		});
		
		
	}

}
