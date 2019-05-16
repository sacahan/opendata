package com.udn.ntpc.od.core.service.data.apiinfo.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.core.service.cfg.DataCfgConnectionParamService;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.data.apiinfo.APIConfigService;
import com.udn.ntpc.od.core.service.data.apiinfo.APIDataInfoService;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_METHOD;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_TYPE;
import com.udn.ntpc.od.model.domain.TableFields;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class APIConfigServiceImpl implements APIConfigService {

    @Resource(name = "apiDataInfoServices")
    private Map<String, APIDataInfoService> apiDataInfoServices;

    @Autowired
    private DataCfgService dataCfgService;
    
    @Autowired
    private DataCfgConnectionParamService dataCfgConnectionParamService;

    @Override
    public Map<String, Object> testConnection(String dataSetOid, Map<String, String> params) {
        return testConnection(dataSetOid, new DataCfgAPIConnectionParams(params));
    }

    @Override
    public Map<String, Object> testConnection(String dataSetOid, DataCfgAPIConnectionParams params) {
    	findByDataSetOid(dataSetOid);
    	Map<String, Object> result = testConnection(params);
        dataCfgService.saveDataCfgConnectionParams(dataSetOid, params);
        return result;
     }

    @Override
    public Map<String, Object> testConnection(DataCfgAPIConnectionParams params) {
    	APIKey_API_TYPE apiType =  APIKey_API_TYPE.valueOf(params.getValue(APIKey.API_TYPE));
        String apiUrl = params.getValue(APIKey.API_URL);
        APIKey_API_METHOD apiMethod = APIKey_API_METHOD.valueOf(params.getValue(APIKey.API_METHOD));
    	
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(apiType.name());
    	String data = apiDataInfoService.apiData(apiUrl, apiMethod, (Object)null);
        if (StringUtils.isBlank(data)) {
        	throw new OpdException(String.format("API連線失敗，請檢查連線設定或確認有無資料內容！(%s)", apiUrl));
        }
        
        Map<String, TableFields> rootFields = apiDataInfoService.getFieldConfigs(data);
        
		Map<String, Object> result = new HashMap<>();
		result.put(ConnectionParam.RAWDATA, data);
		result.put(ConnectionParam.FIELDS, rootFields);
		return result;
    }
    
    @Override
    public CustomResult<String> save(String dataSetOid, String sort, Date startTime, DataFieldCfgsDto dataFieldCfgsDto) {
        return save(dataSetOid, sort, startTime, null, dataFieldCfgsDto);
    }

    @Override
    public CustomResult<String> save(String dataSetOid, String sort, Date startTime, String rootPath, DataFieldCfgsDto dataFieldCfgsDto) {
        DataCfgAPIConnectionParams params = dataCfgConnectionParamService.findAPIByDataSetOid(dataSetOid);
        params.setValue(APIKey.SORT, sort);
        params.setValue(APIKey.START_TIME, DateFormatUtils.format(startTime, ConnectionParam.DATE_TIME_PATTERN));
        params.setValue(APIKey.ROOT_PATH, StringUtils.isBlank(rootPath)? APIKey.ROOT_PATH.getDefaultValue(): rootPath);
        return save(dataSetOid, params, dataFieldCfgsDto);
    }
    
    @Override
    public CustomResult<String> save(String dataSetOid, DataCfgAPIConnectionParams params, DataFieldCfgsDto dataFieldCfgsDto) {
    	findByDataSetOid(dataSetOid);
        
        if (CollectionUtils.isEmpty(dataFieldCfgsDto))
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2070002_EXCEPTION));
        
        testConnection(params);

        dataCfgService.saveDataCfgConnectionParams(dataSetOid, params);
        dataCfgService.saveDataFieldCfgsDto(dataSetOid, dataFieldCfgsDto);

        return CustomResult.result(true, "儲存成功");
    }

    private DataCfg findByDataSetOid(String dataSetOid) {
        DataCfg dataCfg = dataCfgService.findByDataSetOid(dataSetOid);
        if (dataCfg == null) {
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        }
        return dataCfg;
    }
    
}
