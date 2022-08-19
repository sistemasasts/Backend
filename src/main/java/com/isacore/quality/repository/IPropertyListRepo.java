package com.isacore.quality.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.PropertyList;

@Repository
public interface IPropertyListRepo extends JpaRepository<PropertyList, String>{

	@Query(value = "select new com.isacore.quality.model.PropertyList(pl.idProperty, pl.nameProperty, pl.typeProperty, pl.typeProperty2) from propertylist pl", nativeQuery = true)
	List<PropertyList> findAllOnlyProperty();
	
	@Query(value = "select new com.isacore.quality.model.PropertyList(pl.idProperty, pl.nameProperty, pl.typeProperty, pl.typeProperty2) from propertylist pl where pl.idProperty = :idProperty", nativeQuery = true)
	PropertyList findOneOnlyPropertyById(@Param("idProperty") String idProperty);
	
	Optional<PropertyList> findByIdProperty(String id);
	
	List<PropertyList> findByIdPropertyNotIn(Collection<String> idProperty);
	
	@Query(value = "SELECT NEXT VALUE FOR propertyList_secuence", nativeQuery = true)
	int secuencialSiguiente();
	
}