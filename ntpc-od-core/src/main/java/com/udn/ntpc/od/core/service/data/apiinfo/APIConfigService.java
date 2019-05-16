package com.udn.ntpc.od.core.service.data.apiinfo;

import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;

import java.util.Date;
import java.util.Map;

public interface APIConfigService {
	Map<String, Object> testConnection(String dataSetOid, Map<String, String> params);

	Map<String, Object> testConnection(String dataSetOid, DataCfgAPIConnectionParams params);

	Map<String, Object> testConnection(DataCfgAPIConnectionParams params);

	CustomResult<String> save(String dataSetOid, String sort, Date startTime, DataFieldCfgsDto dataFieldCfgsDto);
	
	CustomResult<String> save(String dataSetOid, String sort, Date startTime, String rootPath, DataFieldCfgsDto dataFieldCfgsDto);

	CustomResult<String> save(String dataSetOid, DataCfgAPIConnectionParams params, DataFieldCfgsDto dataFieldCfgsDto);
}
