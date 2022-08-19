package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.PropertyListNorm;
import com.isacore.quality.model.PropertyListNormPK;

@Repository
public interface IPropertyListNormRepo extends JpaRepository<PropertyListNorm, PropertyListNormPK>{
	
	List<PropertyListNorm> findByPropertyList_IdProperty(String idProperty);
	
	void deleteByPropertyList_IdProperty(String idProperty);
	
	@Transactional
	@Modifying
	@Query(value = "insert into property_list_norm(laboratory_norm_id, propl_id) values(:idNorm, :idProperty)", nativeQuery = true)
	int insertPropertyListNorm(@Param("idNorm")Long idNorm, @Param("idProperty")String idProp);
	
	
	@Modifying
	@Query(value = "delete property_list_norm where propl_id = :idProperty", nativeQuery = true)
	int deleteByPropertyId(@Param("idProperty") String idProperty);
}
