package com.isacore.quality.service;


import java.util.List;

import com.isacore.quality.model.ProcessFlow;
import com.isacore.util.CRUD;

public interface IProcessFlowService extends CRUD<ProcessFlow> {
	
	public List<ProcessFlow> findProcessbysUser(String idUser);
}
