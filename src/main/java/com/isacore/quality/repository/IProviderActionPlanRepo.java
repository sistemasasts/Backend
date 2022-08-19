package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isacore.quality.model.ProviderActionPlan;

public interface IProviderActionPlanRepo extends JpaRepository<ProviderActionPlan, Integer> {

}
