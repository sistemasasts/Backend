package com.isacore.sgc.acta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.sgc.acta.model.Role;
import com.isacore.sgc.acta.repository.IRoleRepo;
import com.isacore.sgc.acta.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService{
	
	@Autowired
	private IRoleRepo roleRepo;

	@Override
	public List<Role> findAll() {
		return this.roleRepo.findAll();		
	}

	@Override
	public Role create(Role role) {
		return this.roleRepo.save(role);
	}

	@Override
	public Role findById(Role r) {
		Optional<Role> o = this.roleRepo.findById(r.getRolName());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public Role update(Role role) {
		return this.roleRepo.save(role);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
