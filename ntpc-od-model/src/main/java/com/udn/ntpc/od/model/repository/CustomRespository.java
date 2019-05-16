package com.udn.ntpc.od.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

@NoRepositoryBean
public interface CustomRespository<T, ID> extends JpaRepository<T, ID> {

    @Query("from #{#entityName}")
    Stream<T> streamFindAll();

}
