package com.udn.ntpc.od.core.service.data.impl;

import com.udn.ntpc.od.common.variables.Profiles;
import com.udn.ntpc.od.core.service.cfg.DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.cfg.SQLServerDataCfgTableInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.SQLServerDataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile(Profiles.NOT_DEV)
@Service
@Transactional
public class SQLServerDataServiceImpl extends AbstractDataServiceImpl {

    @Autowired
    private SQLServerDataCfgTableInfoService dataCfgTableInfoService;
    
    @Autowired
    private SQLServerDataInfoService dbDataInfoService;
	
	@Override
	protected DataCfgTableInfoService getDataCfgTableInfoService() {
		return dataCfgTableInfoService;
	}
	
	@Override
	protected DBDataInfoService getDBDataInfoService() {
		return dbDataInfoService;
	}

}
