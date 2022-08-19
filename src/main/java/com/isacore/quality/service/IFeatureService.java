package com.isacore.quality.service;

import com.isacore.quality.model.Feature;
import com.isacore.util.CRUD;

public interface IFeatureService extends CRUD<Feature>{

	String findFeatureReviewByIdP(int idP);
	
}
