package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.isacore.quality.model.Feature;

public interface IFeatureRepo extends JpaRepository<Feature, Integer>{

	@Query(value = "select f.fea_review from feature f inner join product p on p.fea_id = f.fea_id where p.product_id = :idP", nativeQuery = true)
	String findFeatureReviewByIdP(@Param("idP")int idP);
	
}
