package com.udn.ntpc.od.core.service.data.impl;

import com.udn.ntpc.od.common.variables.Profiles;
import com.udn.ntpc.od.core.service.cfg.DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.cfg.H2DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.H2DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile(Profiles.DEV)
@Service
@Transactional
public class H2DataServiceImpl extends AbstractDataServiceImpl {

    @Autowired
    private H2DataCfgTableInfoService dataCfgTableInfoService;
	
    @Autowired
    private H2DataInfoService dbDataInfoService;
    
	@Override
	protected DataCfgTableInfoService getDataCfgTableInfoService() {
		return dataCfgTableInfoService;
	}
	
	@Override
	protected DBDataInfoService getDBDataInfoService() {
		return dbDataInfoService;
	}

}
