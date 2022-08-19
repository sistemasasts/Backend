package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.Property;
import com.isacore.quality.model.PropertyPK;

@Repository
public interface IPropertyRepo extends JpaRepository<Property, PropertyPK>{

	@Query(value = "select p.propl_id from property p where p.product_id = :idProduct and p.propl_id = :idPropertyList", nativeQuery = true)
	List<Object> validateExistProperty(@Param("idProduct")Integer idProduct, @Param("idPropertyList") String idPropertyList);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "insert into property (product_id, propl_id, property_min, property_max, property_unit, property_view, property_view_hcc, property_update, property_type, property_norm, property_asuser) values (:idProduct, :idPropertyList, :pMin, :pMax, :pUnit, :pView, 0, :pDateUpdate, :pType, :pNorm, :pUser)", nativeQuery = true)
	int createProperty(@Param("idProduct")Integer idProduct, @Param("idPropertyList")String idPropertyList, @Param("pMin") Double pMin, @Param("pMax")Double pMax, @Param("pUnit") String pUnit, @Param("pView")String  pView, @Param("pDateUpdate")String pDateUpdate, @Param("pType") String pType, @Param("pNorm")String pNorm, @Param("pUser")String pUser);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "update property set property_min = :pMin, property_max = :pMax, property_unit = :pUnit, property_view = :pView, property_update = :pDateUpdate, property_type = :pType, property_norm = :pNorm, property_asuser = :pUser where product_id = :idProduct and propl_id = :idPropertyList", nativeQuery = true)
	int updateProperty(@Param("pMin") Double pMin, @Param("pMax")Double pMax, @Param("pUnit") String pUnit, @Param("pView")String  pView, @Param("pDateUpdate")String pDateUpdate, @Param("pType") String pType, @Param("pNorm")String pNorm, @Param("pUser")String pUser, @Param("idProduct")Integer idProduct, @Param("idPropertyList")String idPropertyList);
	
	@Query(value = "select * from property pp where pp.product_id = :idProduct and pp.propl_id = :idPropertyList", nativeQuery = true)
	Property findPropertyByIdPropertyListandIdProduct(@Param("idProduct")Integer idProduct, @Param("idPropertyList") String idPropertyList);
	
	List<Property> findByProductOrderByPropertyList_NamePropertyAsc(Product product);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "delete property where product_id = :idProduct and propl_id = :idPropertyList", nativeQuery = true)
	int deleteByProductAndProperty(@Param("idProduct") Integer idProduct, @Param("idPropertyList")String idPropertyList);
	
	List<Property> findByPropertyList_IdProperty(String idProperty);
	
}
