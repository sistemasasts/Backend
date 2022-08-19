package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.Formulation;

@Repository
public interface IFormulationRepo extends JpaRepository<Formulation, Integer>{

	@Query(value = "select ltrim(replace(fi.fi_description,'%','')), f.form_multifactor, f.form_unit, f.form_value from formulation f  inner join product p on p.product_id = f.product_id inner join formulationitem fi on f.fi_id = fi.fi_id where rtrim(replace(p.product_sap_code,'.0','')) = :idPSap and f.form_id = :idF", nativeQuery = true)
	List<Object[]> searchFormulationByProductFormuType(@Param("idPSap")String idPSap, @Param("idF")Integer idF);
	
	@Query(value = "select f.form_value from formulation f where f.fi_id = :idFi and f.form_id = :idFl and f.product_id = :idProd", nativeQuery = true)
	List<Object> validateExistFormulation(@Param("idFi")Integer idFi, @Param("idFl")Integer idFl, @Param("idProd")Integer idProd);
	
	@Query(value ="select distinct fl.form_id,fl.form_description from formulalist fl inner join formulation f on f.form_id = fl.form_id inner join product p on p.product_id = f.product_id where p.product_id = :idProd", nativeQuery = true)
	List<Object[]> getTypeFormulationsListByProduct(@Param("idProd") Integer idProd);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "insert into formulation (fi_id, form_id, product_id, form_multifactor, form_value, form_update_excel, form_update, form_asuser) values (:idFi, :idFl, :idProd, :multiFac, :valueF, :dateUE, :dateU, :asUser);", nativeQuery = true)
	int createFormulation(@Param("idFi")Integer idFi, @Param("idFl")Integer idFl, @Param("idProd")Integer idProd, @Param("multiFac")Integer multiFac, @Param("valueF")Double valueF, @Param("dateUE")String dateUE, @Param("dateU")String dateU, @Param("asUser")String asUser);
	
	@Transactional(propagation = Propagation.NESTED)
	@Modifying
	@Query(value = "update formulation set form_multifactor=:multiFac , form_value=:valueF, form_update_excel=:dateUE, form_update=:dateU, form_asuser=:asUser  where fi_id = :idFi and form_id = :idFl and product_id = :idProd", nativeQuery = true)
	int updateFormulation(@Param("multiFac")Integer multiFac, @Param("valueF")Double valueF, @Param("dateUE")String dateUE, @Param("dateU")String dateU, @Param("asUser")String asUser, @Param("idFi")Integer idFi, @Param("idFl")Integer idFl, @Param("idProd")Integer idProd);
	
}
