package com.isacore.committee.tx;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.isacore.sgc.acta.model.Committee;
import com.isacore.sgc.acta.service.ICommitteeService;
import com.isacore.util.WebRequestIsa;

@Component
public class TxCommittee {
	
	@Autowired
	ICommitteeService committeeService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<Object> TxQRgetCommittees (WebRequestIsa wri){
		logger.info("> TX: TxQRgetCommittees");
		
		List<Committee> list = this.committeeService.findAll();
		if(list.isEmpty() || list == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		}
	}
	
	
}
