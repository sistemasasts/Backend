package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.isacore.quality.model.ProcessFlow;




public interface IProcessFlowRepo extends JpaRepository<ProcessFlow, Integer> {

}
