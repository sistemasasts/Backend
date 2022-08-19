package com.isacore.sgc.acta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isacore.sgc.acta.model.ActionPlan;

@Repository
public interface IActionPlanRepo extends JpaRepository<ActionPlan, String>{

	@Query(value = "select new com.isacore.sgc.acta.model.ActionPlan(a.idPlan, a.description, a.startDate, a.deadLine,"
			+ "a.executed, a.executionDate) from actionPlan a inner join a.minute as am where am.idMinute = :idMinute", nativeQuery = true)
	List<ActionPlan> findPlanByIdMinute(@Param("idMinute") String idMinute);
	
}
