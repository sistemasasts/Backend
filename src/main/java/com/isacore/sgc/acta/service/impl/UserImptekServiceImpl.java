package com.isacore.sgc.acta.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.quality.model.Area;
import com.isacore.sgc.acta.model.Employee;
import com.isacore.sgc.acta.model.Role;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IRoleRepo;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.sgc.acta.service.IUserImptekService;

@Service
public class UserImptekServiceImpl implements IUserImptekService{

	@Autowired
	private IUserImptekRepo repo;
	
	@Autowired
	private IRoleRepo repoRole;
	
	@Override
	public List<UserImptek> findAll() {		
		return this.repo.findAll();
	}

	@Override
	public UserImptek create(UserImptek user) {
		return this.repo.save(user);
	}

	@Override
	public UserImptek findById(UserImptek u) {
		Optional<UserImptek> o = this.repo.findById(u.getIdUser());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public UserImptek update(UserImptek user) {
		return this.repo.save(user);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public UserImptek findByUserImptek(String nickname) {
		
		List<Object[]> rowSql = this.repo.findUserByNickname(nickname);
		
		if(!(rowSql.isEmpty() || rowSql == null)) {
			Object[] o = rowSql.get(0);
	 		
			UserImptek ui = new UserImptek();
			ui.setIdUser((String)o[0]);
			LocalDateTimeConverter ldtc = new LocalDateTimeConverter();
			ui.setLastAccess(ldtc.convertToEntityAttribute((Timestamp)o[1]));			
			ui.setLastKeyDateChange(ldtc.convertToEntityAttribute((Timestamp)o[2]));
			ui.setNickName((String)o[3]);
			
			Employee emp = new Employee();
			emp.setCiEmployee((String)o[4]);
			
			Optional<Role> op = this.repoRole.findById((String)o[5]);
			if(op.isPresent())
				ui.setRole(op.get());
			
			//ui.setRole(this.repoRole.findOne((String)o[6]));
			
			emp.setName((String)o[7]);
			emp.setLastName((String)o[8]);
			emp.setJob((String)o[9]);
			emp.setState((Boolean)o[10]);
			emp.setEmail((String)o[11]);
			Area a= new Area();
			
			a.setNameArea((String) o[12]);
			emp.setArea(a);
			ui.setEmployee(emp);
			
			return ui;

		}
		
		return null;
	}

	@Override
	public UserImptek findOnlyUserByNickname(String nickname) {
		return this.repo.findOnlyUserByNickname(nickname);
	}

	

}
