package com.isacore.quality.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.mail.Mail;
import com.isacore.quality.model.Notification;
import com.isacore.quality.repository.INotificationRepo;
import com.isacore.quality.service.INotificationService;

@Service
public class NotificationImpl implements INotificationService{

	@Autowired
	private INotificationRepo repo;
	
	@Autowired
	private Mail email;
	
	@Override
	public List<Notification> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification create(Notification obj) {
		return this.repo.save(obj);
	}

	@Override
	public Notification findById(Notification id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Notification update(Notification obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}
	
	@Override
	public List<Notification> findbyIdUserAndState(String idUser, String state) {
		List<Notification>nl=this.repo.findByIdUserAndState(idUser, state);		
		return nl;
	}

	@Override
	public List<Notification> findByIdProcess(Integer idProcess) {
		List<Notification>nl=this.repo.findByIdProcess(idProcess);	
		return nl;
	}
	
	@Override
	public List<Notification> findByIdProcessAndState(Integer idProcess, String state) {
		List<Notification> nl=this.repo.findByIdProcessAndState(idProcess, state);	
		return nl;
	}

}
