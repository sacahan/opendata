package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.core.service.cfg.SQLServerDataCfgTableInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.SQLServerDataInfoService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SQLServerDataCfgTableInfoServiceImpl extends AbstractDataCfgTableInfoServiceImpl<DataCfgTableInfo, String> implements SQLServerDataCfgTableInfoService {

	@Autowired
	protected SQLServerDataInfoService dbDataInfoService;

	@Override
	protected DBDataInfoService getDBDataInfoService() {
		return dbDataInfoService;
	}

}
