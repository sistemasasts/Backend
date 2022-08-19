package com.isacore.quality.repository; 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.HccHead;

@Repository
public interface IHccHeadRepo extends JpaRepository<HccHead, Long>{

	@Query(value = "select hh.id, hh.hcch_batch, p.product_name, p.product_type, hh.hcch_date, hh.hcch_analysis, hh.hcch_sapcode, hh.hcch_periodicity from hcchead hh inner join product p on hh.product_id = p.product_id where p.product_type = :tp order by hcch_date desc", nativeQuery = true)
	List<Object[]> findOnlyHccHead(@Param("tp")String tp);
	
	@Query(value = "select hh.id, hh.hcch_batch, p.product_name, p.product_type, hh.hcch_date, hh.hcch_analysis, hh.hcch_sapcode, hh.hcch_periodicity from hcchead hh inner join product p on hh.product_id = p.product_id order by hcch_date desc", nativeQuery = true)
	List<Object[]> findOnlyHccHead();
	
}
	