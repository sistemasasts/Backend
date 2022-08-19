package com.isacore.quality.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.service.INonconformingProductService;

@RestController
@RequestMapping(value = "/pncs")
public class PncController {
	
	@Autowired 
	INonconformingProductService service;
	
	//MediaType.APPLICATION_OCTET_STREAM_VALUE permite enviar el arreglo de bits en lugar de json
		@GetMapping(value="/generarReporte/{idPnc}", produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
		public ResponseEntity<byte[]> generarReporte( @PathVariable Integer idPnc) {
			byte[] data= null;
			data = service.generarReportePnc(idPnc);
			return new ResponseEntity<byte[]>(data, HttpStatus.OK);
		}

}
