package com.isacore;

import org.springframework.data.repository.CrudRepository;


public interface RepositorioBaseId<T extends EntidadBaseId> extends CrudRepository<T, Long>  {

}
