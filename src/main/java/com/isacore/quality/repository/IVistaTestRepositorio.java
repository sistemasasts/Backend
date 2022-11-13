package com.isacore.quality.repository;

import com.isacore.quality.model.VistaTest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface IVistaTestRepositorio extends PagingAndSortingRepository<VistaTest, String> {

}
