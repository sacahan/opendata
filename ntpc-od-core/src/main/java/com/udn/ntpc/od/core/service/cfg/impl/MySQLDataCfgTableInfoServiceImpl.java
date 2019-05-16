package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.core.service.cfg.MySQLDataCfgTableInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.MySQLDataInfoService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MySQLDataCfgTableInfoServiceImpl extends AbstractDataCfgTableInfoServiceImpl<DataCfgTableInfo, String> implements MySQLDataCfgTableInfoService {
    
    @Autowired
    protected MySQLDataInfoService dbDataInfoService;
	 
	@Override
	protected DBDataInfoService getDBDataInfoService() {
		return dbDataInfoService;
	}

 }
