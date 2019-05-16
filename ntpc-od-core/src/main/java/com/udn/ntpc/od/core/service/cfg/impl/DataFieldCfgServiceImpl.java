package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.core.service.cfg.DataFieldCfgService;
import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;
import com.udn.ntpc.od.model.cfg.repository.DataFieldCfgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataFieldCfgServiceImpl extends AbstractCustomServiceImpl<DataFieldCfg, String> implements DataFieldCfgService {

    @Autowired
    private DataFieldCfgRepository repository;

    @Override
    public DataFieldCfgs findByDataCfgOid(String dataCfgOid) {
        DataFieldCfgs dataFieldCfgs = new DataFieldCfgs();
        List<DataFieldCfg> result = repository.findByDataCfgOidOrderByFieldOrder(dataCfgOid);
        dataFieldCfgs.addAll(result);
        return dataFieldCfgs;
    }
    
    @Override
    public DataFieldCfgs findByDataSetOid(String dataCfgOid) {
        DataFieldCfgs dataFieldCfgs = new DataFieldCfgs();
        List<DataFieldCfg> result = repository.findByDataSetOidOrderByFieldOrder(dataCfgOid);
        dataFieldCfgs.addAll(result);
        return dataFieldCfgs;
    }
    
}
