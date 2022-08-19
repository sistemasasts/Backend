package com.isacore.quality.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.quality.model.NormState;

@Repository
public interface ILaboratoryNormRepo extends JpaRepository<LaboratoryNorm, Long>{
	
	List<LaboratoryNorm> findByStateIn(Collection<NormState> state);
	
	List<LaboratoryNorm> findByIdNotInAndStateIn(Collection<Long> id, Collection<NormState> state);
}
