package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.Feature;
import com.isacore.quality.repository.IFeatureRepo;
import com.isacore.quality.service.IFeatureService;

@Service
public class FeatureServiceImpl implements IFeatureService{

	@Autowired
	private IFeatureRepo repo;
	
	@Override
	public String findFeatureReviewByIdP(int idP) {
		
		String review = this.repo.findFeatureReviewByIdP(idP);
		
		if(review == null) return "";
		else return review;
	}

	@Override
	public List<Feature> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Feature create(Feature f) {
		return this.repo.save(f);
	}

	@Override
	public Feature findById(Feature fea) {
		Optional<Feature> f = this.repo.findById(fea.getIdFeature());
		if(f.isPresent())
			return f.get();
		else
			return null;
	}

	@Override
	public Feature update(Feature f) {
		return this.repo.save(f);
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return true;
		
	}

}
