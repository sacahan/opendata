package com.udn.ntpc.od.core.service.impl;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.stream.Stream;

@Transactional
public abstract class AbstractCustomServiceImpl<T, ID extends Serializable> extends CommonServiceImpl<T, ID> implements CustomService<T, ID> {

    @Autowired
    private CustomRespository<T, ID> repository;

    @Override
    public Stream<T> streamFindAll() {
        return repository.streamFindAll();
    }

}
