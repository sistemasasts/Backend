package com.isacore.quality.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.ReportHeadT;
import com.isacore.quality.repository.IReportHeadTRepo;
import com.isacore.quality.service.IReportHeadTService;

@Service
public class ReportHeadTServiceImpl implements IReportHeadTService{
	
	@Autowired
	private IReportHeadTRepo repo;

	@Override
	public List<ReportHeadT> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportHeadT create(ReportHeadT obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportHeadT findById(ReportHeadT id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportHeadT update(ReportHeadT obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public ReportHeadT findHeadByTypeReport(ReportHeadT rht) {
		return this.repo.findHeadByTypeReport(rht.getType());
	}

}
