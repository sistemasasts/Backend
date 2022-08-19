package com.isacore.sgc.acta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.sgc.acta.model.MeetingMinute;
import com.isacore.sgc.acta.repository.IMeetingMinuteRepo;
import com.isacore.sgc.acta.service.IMeetingMinuteService;

@Service
public class MeetingMinuteServiceImpl implements IMeetingMinuteService{
	
	@Autowired
	private IMeetingMinuteRepo repo;

	@Override
	public List<MeetingMinute> findAll() {
		return this.repo.findAll();
	}

	@Override
	public MeetingMinute create(MeetingMinute mm) {
		return this.repo.save(mm);
	}

	@Override
	public MeetingMinute findById(MeetingMinute m) {
		Optional<MeetingMinute> o = this.repo.findById(m.getIdMinute());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public MeetingMinute update(MeetingMinute mm) {
		return this.repo.save(mm);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
