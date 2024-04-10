package com.isacore.quality.service.reclamoMP;

import java.util.List;

import com.isacore.quality.model.reclamoMP.Problem;
import com.isacore.util.CRUD;

public interface IProblemService extends CRUD<Problem> {
	
	List<Problem> dataTratamientImagesReport(List<Problem> problems);
}
