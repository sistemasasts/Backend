package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.PropertyList;
import com.isacore.quality.model.PropertyListNorm;

public interface IPropertyListNormService {

	void associatePropertyListAndNorms(List<PropertyListNorm> norms, PropertyList propertyList );
}
