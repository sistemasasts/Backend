package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.quality.model.Complaint;
import com.isacore.quality.model.Problem;
import com.isacore.quality.repository.IComplaintRepo;
import com.isacore.quality.repository.IProblemRepo;
import com.isacore.quality.service.IProblemService;

@Component
public class ProblemImpl implements IProblemService{

	@Autowired
	private IProblemRepo repo;
	
	@Override
	public List<Problem> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Problem create(Problem obj) {
		return this.repo.save(obj);
	}

	@Override
	public Problem findById(Problem obj) {
		Optional<Problem> com = this.repo.findById(obj.getIdProblem());
		if (com.isPresent())
			return com.get();
		else
			return null;
	}

	@Override
	public Problem update(Problem obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public List<Problem> dataTratamientImagesReport(List<Problem> problems) {
	
		if(problems.size()!=0) {
			for (Problem problem : problems) {
				String tmp[]=problem.getPictureStringB64().split(",");
				problem.setPictureStringB64(tmp[1]);							
				
			}
		}
		return problems;
	}
	

}
