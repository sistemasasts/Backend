package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.InformationAditionalFile;

@Repository
public interface IApprobationCriteriaFileRepo extends JpaRepository<InformationAditionalFile, Long> {

}
