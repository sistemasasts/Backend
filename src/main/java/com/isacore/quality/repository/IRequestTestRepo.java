package com.isacore.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.RequestTest;

@Repository
public interface IRequestTestRepo extends JpaRepository<RequestTest, Integer>{

}
