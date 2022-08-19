package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.Notification;
import com.isacore.util.CRUD;

public interface INotificationService extends CRUD<Notification>{

	List<Notification> findbyIdUserAndState(String idUser, String state);
	
	List<Notification> findByIdProcess(Integer idProcess);
	
	List<Notification> findByIdProcessAndState(Integer idProcess, String state);
}
