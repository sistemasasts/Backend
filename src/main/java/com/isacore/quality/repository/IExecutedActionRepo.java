package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isacore.quality.model.ExecutedAction;

public interface IExecutedActionRepo extends JpaRepository<ExecutedAction, Integer> {

}
