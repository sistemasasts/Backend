package com.isacore.quality.dto;

import java.util.List;

import com.isacore.quality.model.ActionProcess;
import com.isacore.quality.model.Notification;
import com.isacore.quality.model.ProcessFlow;

public class TrayDto {

	private String user;
	
	private ProcessFlow process;
	
	private Notification noti;
	
	private ActionProcess actionProcess;
	
	private String stateReply;
	
	private String userReplay;
	
	private String userFullNameReplay;
	
	private ActionProcess actionProcessReply;
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ProcessFlow getProcess() {
		return process;
	}

	public void setProcess(ProcessFlow process) {
		this.process = process;
	}

	public Notification getNoti() {
		return noti;
	}

	public void setNoti(Notification noti) {
		this.noti = noti;
	}

	public ActionProcess getActionProcess() {
		return actionProcess;
	}

	public void setActionProcess(ActionProcess actionProcess) {
		this.actionProcess = actionProcess;
	}

	public String getStateReply() {
		return stateReply;
	}

	public void setStateReply(String stateReply) {
		this.stateReply = stateReply;
	}

	public String getUserReplay() {
		return userReplay;
	}

	public void setUserReplay(String userReplay) {
		this.userReplay = userReplay;
	}

	public ActionProcess getActionProcessReply() {
		return actionProcessReply;
	}

	public void setActionProcessReply(ActionProcess actionProcessReply) {
		this.actionProcessReply = actionProcessReply;
	}

	public String getUserFullNameReplay() {
		return userFullNameReplay;
	}

	public void setUserFullNameReplay(String userFullNameReplay) {
		this.userFullNameReplay = userFullNameReplay;
	}
	
	
		
	
	
}
