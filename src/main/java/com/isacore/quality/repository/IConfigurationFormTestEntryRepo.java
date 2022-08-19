package com.isacore.quality.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.ConfigurationFormTestEntryMP;

@Repository
public interface IConfigurationFormTestEntryRepo extends JpaRepository<ConfigurationFormTestEntryMP, Integer> {

	@Query(value = "select distinct product_typetxt from product where product_type='MATERIA_PRIMA'", nativeQuery = true)
	List<Object[]> findCatalogByTypeText();

	List<ConfigurationFormTestEntryMP> findByProductTypeText(String productTypeText);
	
	Optional<ConfigurationFormTestEntryMP> findByProductTypeTextAndProperty_IdProperty(String productTypeText, String idProperty);
	
	
}
