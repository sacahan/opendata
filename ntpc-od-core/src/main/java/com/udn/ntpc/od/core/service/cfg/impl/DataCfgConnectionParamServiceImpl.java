package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.core.service.cfg.DataCfgConnectionParamService;
import com.udn.ntpc.od.core.service.impl.CommonServiceImpl;
import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.repository.DataCfgConnectionParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataCfgConnectionParamServiceImpl extends CommonServiceImpl<DataCfgConnectionParam, String> implements DataCfgConnectionParamService {

    @Autowired
    private DataCfgConnectionParamRepository repository;

    @Override
    public <T> T parameter(String id) {
        return parameter(id, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parameter(String id, Object defaultValue) {
        DataCfgConnectionParam result = getOne(id);
        if (result == null) {
            return (T) defaultValue;
        }
        if (result.getParamValue() != null) {
            return (T) result.getParamValue();
        }
        return (T) defaultValue;
    }

    @Override
    public DataCfgConnectionParams findByDataSetOid(String dataSetOid) {
        List<DataCfgConnectionParam> params = repository.findByDataSetOid(dataSetOid);
        return new DataCfgConnectionParams(params);
    }
    
    @Override
    public DataCfgAPIConnectionParams findAPIByDataSetOid(String dataSetOid) {
    	List<DataCfgConnectionParam> params = repository.findByDataSetOid(dataSetOid);
    	return new DataCfgAPIConnectionParams(params);
    }
    
    @Override
    public DataCfgConnectionParams findByDataCfgOid(String dataCfgOid) {
        List<DataCfgConnectionParam> params = repository.findByDataCfgOid(dataCfgOid);
        return new DataCfgConnectionParams(params);
    }
    
}
