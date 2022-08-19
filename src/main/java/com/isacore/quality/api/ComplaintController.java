package com.isacore.quality.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.quality.model.Complaint;
import com.isacore.quality.service.IComplaintService;

@RestController
@RequestMapping(value = "/complaints")
public class ComplaintController {

	@Autowired
	private IComplaintService service;
	
	@GetMapping("/close/{id}")
	public ResponseEntity<Complaint> closeComplaint(@PathVariable("id") Integer id) {
		Complaint complaintClose = service.close(id);
		return new ResponseEntity<Complaint>(complaintClose, HttpStatus.OK);
	}
}
