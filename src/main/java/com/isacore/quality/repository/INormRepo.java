package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Norm;

@Repository
public interface INormRepo extends JpaRepository<Norm, Integer>{

	//@Query("select new com.isacore.quality.model.Norm(n.idNorm, n.nameNorm, n.aplication, n.description, n.kind) from norm n where n.kind = :kindNorm")
	//List<Norm> findByKindNorm(@Param("kindNorm") String kindNorm);
	
//	@Query("from norm where kind = :kindNorm")
//	List<Norm> findByKindNorm(@Param("kindNorm") String kindNorm);
	
}
