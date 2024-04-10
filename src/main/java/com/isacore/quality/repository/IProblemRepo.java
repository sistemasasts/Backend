package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isacore.quality.model.reclamoMP.Problem;

public interface IProblemRepo extends JpaRepository<Problem, Integer> {

}
