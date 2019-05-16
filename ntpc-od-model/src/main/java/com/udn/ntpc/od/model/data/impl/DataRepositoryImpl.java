package com.udn.ntpc.od.model.data.impl;

import com.udn.ntpc.od.model.data.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DataRepositoryImpl implements DataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

}
