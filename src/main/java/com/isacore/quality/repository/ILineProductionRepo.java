package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.LineProduction;

@Repository
public interface ILineProductionRepo extends JpaRepository<LineProduction, Integer> {

}
