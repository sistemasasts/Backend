package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.Problem;
import com.isacore.util.CRUD;

public interface IProblemService extends CRUD<Problem> {
	
	List<Problem> dataTratamientImagesReport(List<Problem> problems);
}
