package com.isacore.quality.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.ActionProcess;
import com.isacore.quality.repository.IActionProcessRepo;
import com.isacore.quality.service.IActionProcessService;

@Service
public class ActionProcessImpl implements IActionProcessService {

	@Autowired
	private IActionProcessRepo repo;

	@Override
	public List<ActionProcess> findAll() {
		List<ActionProcess> apTmp = this.repo.findAll();
		return apTmp;
	}

	@Override
	public ActionProcess create(ActionProcess obj) {
		return this.repo.save(obj);
	}

	@Override
	public ActionProcess findById(ActionProcess ac) {
		Optional<ActionProcess> aPTmp = this.repo.findById(ac.getIdactionProcess());
		return aPTmp.get();
	}

	@Override
	public ActionProcess update(ActionProcess obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return true;

	}

	// Generar ID String co el uso de la fecha
	public String generateId() {
		String id = null;
		LocalDateTime cc = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		id = cc.format(formatter);
		id = id.replaceAll("[^\\dA-Za-z]", "");

		return id;
	}
}
