package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.core.service.cfg.H2DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.H2DataInfoService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 測試用
 *
 */
@Service
@Transactional
public class H2DataCfgTableInfoServiceImpl extends AbstractDataCfgTableInfoServiceImpl<DataCfgTableInfo, String> implements H2DataCfgTableInfoService {

	@Autowired
	protected H2DataInfoService dbDataInfoService;

	@Override
	protected DBDataInfoService getDBDataInfoService() {
		return dbDataInfoService;
	}

}
