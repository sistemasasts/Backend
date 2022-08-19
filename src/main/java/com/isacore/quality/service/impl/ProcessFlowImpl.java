package com.isacore.quality.service.impl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.ActionProcess;
import com.isacore.quality.model.FileDocument;
import com.isacore.quality.model.ProcessFlow;
import com.isacore.quality.repository.IProcessFlowRepo;
import com.isacore.quality.service.IProcessFlowService;
import com.isacore.util.PassFileToRepository;

@Service
public class ProcessFlowImpl  implements IProcessFlowService{

	@Autowired
	private IProcessFlowRepo repo;
	
	@Override
	public List<ProcessFlow> findAll() {
		List<ProcessFlow> pfTmp = this.repo.findAll();
		return pfTmp;
	}

	@Override
	public ProcessFlow create(ProcessFlow obj) {
		LocalDate date= LocalDate.now();
		obj.setStartDate(date);
		
		List<ActionProcess> lap=obj.getListActionsProcess();
		if(!lap.isEmpty() || lap != null) {
			for(ActionProcess ac: lap) {
				ac.setIdactionProcess(generateId());
				ac.setDateAP(LocalDateTime.now());	
				for(FileDocument fd: ac.getListFileDocument()) {
					String uri=PassFileToRepository.base64ToFile(fd.getBase64File(), fd.getName(), fd.getExtension());
					fd.setUrl(uri);					
				}
			}
		}				
		return this.repo.save(obj);
	}

	@Override
	public ProcessFlow findById(ProcessFlow pf) {
		Optional<ProcessFlow> pfTmp = this.repo.findById(pf.getIdProcess());
		return pfTmp.get();
	}

	@Override
	public ProcessFlow update(ProcessFlow obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}
	
	// Generar ID String co el uso de la fecha
	private String generateId() {
		String id=null;
		LocalDateTime cc = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		id=cc.format(formatter);
		id= id.replaceAll("[^\\dA-Za-z]", "");
				
		return id;
	}

	@Override
	public List<ProcessFlow> findProcessbysUser(String idUser) {
		List<ProcessFlow> presult = new ArrayList<ProcessFlow>();
		List<ProcessFlow> pfTmp = this.repo.findAll();
		for(ProcessFlow pw:pfTmp) {
			boolean flag=false;
			for(ActionProcess ac: pw.getListActionsProcess()) {
				if(ac.getUserImptek().equals(idUser)) {
					flag=true;
				}
				if(flag){
					presult.add(pw);
				}
			}
		}
		return presult;
	}
	
	



}
