package com.udn.ntpc.od.core.service.system.impl;

import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.core.service.system.CoordinateMappingService;
import com.udn.ntpc.od.model.system.domain.CoordinateMapping;
import com.udn.ntpc.od.model.system.repository.CoordinateMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoordinateMappingServiceImpl extends AbstractCustomServiceImpl<CoordinateMapping, String> implements CoordinateMappingService {

    @Autowired
    private CoordinateMappingRepository repository;

    @Override
    public CoordinateMapping findByDataSetOid(String dataSetOid) {
        return repository.findByDataSetOid(dataSetOid);
    }

}
