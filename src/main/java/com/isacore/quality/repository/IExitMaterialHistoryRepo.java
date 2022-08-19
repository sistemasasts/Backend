package com.isacore.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.ExitMaterialHistory;

@Repository
public interface IExitMaterialHistoryRepo extends JpaRepository<ExitMaterialHistory, Integer> {

	List<ExitMaterialHistory> findByNcpID(Integer id);
}
