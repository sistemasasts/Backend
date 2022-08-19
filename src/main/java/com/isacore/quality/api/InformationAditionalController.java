package com.isacore.quality.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.isacore.quality.dto.InformationAditionalFileDTO;
import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.service.IInformationAditionalService;

@RestController
@RequestMapping(value = "/informationAditional")
public class InformationAditionalController {

	@Autowired
	private IInformationAditionalService service;
	
	@PostMapping
	public ResponseEntity<InformationAditional> addinformationAditionalFile(@RequestPart("info") String info, @RequestPart("file") MultipartFile file) throws IOException {
		InformationAditional infoRegister = service.addImage(info, file.getBytes());
		return new ResponseEntity<InformationAditional>(infoRegister, HttpStatus.CREATED);
	}
	
	@GetMapping("/readFiles/{id}")
	public ResponseEntity<List<InformationAditionalFileDTO>> readFilesBase64(@PathVariable("id") Long id) throws IOException {
		List<InformationAditionalFileDTO> files = service.readFiles(id);
		return new ResponseEntity<List<InformationAditionalFileDTO>>(files, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		service.deleteByCriteriaId(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<InformationAditional> updateInfo(@RequestBody InformationAditional info) {
		InformationAditional infoUpdated = service.update(info);
		return new ResponseEntity<InformationAditional>(infoUpdated, HttpStatus.OK);
	}

}
