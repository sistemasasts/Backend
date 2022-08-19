package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.Test;

@Repository
public interface ITestRepo extends JpaRepository<Test, Integer> {

	@Query("select new com.isacore.quality.model.Test(t.idProperty ,avg(t.resultTest)) from test t where t.batchTest = :batch and t.prommissing = true group by t.idProperty")
	List<Test> findByBatch(@Param("batch") String batch);
	
	@Query(value = "select * from test where test_batch = :batch order by test_promissing desc", nativeQuery = true)
	List<Test> findByBatchAll(@Param("batch") String batch);

	@Query(value = "select * from test where test_batch = :batch and test_promissing = 0", nativeQuery = true)
	List<Test> findByBatchAndPromissing(@Param("batch") String batch);

	@Query(value = "select * from test where  test_idproduct = :idP and test_property_code=:idProp ", nativeQuery = true)
	List<Test> findByIdProduct(@Param("idP") Integer idP, @Param("idProp") String idProp);

	@Query(value = "Select * from test where test_batch like '%MP%' OR test_batch like '%DEV%'", nativeQuery = true)
	List<Test> findByBatchMP(@Param("batch") String batch);
	
	@Query(value = "select * from test where test_batch = :batch and test_idproduct = :idP", nativeQuery = true)
	List<Test> findByBatchAndIdProduct(@Param("batch") String batch, @Param("idP") Integer idP);
	
	@Query(value = "select hh.hcch_date_order as 'dateOrder', p.product_name,p.product_typetxt as 'nameProduct', hh.hcch_batch as 'batch', hh.hcch_of as 'of', hd.hccd_test_result as 'result', hd.hccd_test_result_view as 'resultView', pl.propl_name as 'nameProperty' from hcchead hh\r\n" + 
			"	inner join hccdetail hd on hh.hcch_sapcode=hd.hcch_sapcode\r\n" + 
			"	inner join product p on p.product_id=hh.product_id\r\n" + 
			"	inner join propertylist pl on pl.propl_id=hd.hccd_prop_id\r\n" + 
			"	where p.product_type='PRODUCTO_TERMINADO' and hcch_date_order between :dateIni and :dateFin \r\n" + 
			"	order by p.product_typetxt", nativeQuery = true)
	List<Object[]> dataReport(@Param("dateIni") String dateIni, @Param("dateFin") String dateFin);


}
