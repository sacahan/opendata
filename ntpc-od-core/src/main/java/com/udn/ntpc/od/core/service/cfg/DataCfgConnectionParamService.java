package com.udn.ntpc.od.core.service.cfg;

import com.udn.ntpc.od.core.service.CommonService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;

public interface DataCfgConnectionParamService extends CommonService<DataCfgConnectionParam, String> {

    /**
     * 用 id 取
     * @param id
     * @return 回應的型態會依據設定的型態回應
     */
    <T> T parameter(String id);
    
    <T> T parameter(String id, Object defaultValue);
    
    DataCfgConnectionParams findByDataSetOid(String dataSetOid);
    DataCfgAPIConnectionParams findAPIByDataSetOid(String dataSetOid);
    
    DataCfgConnectionParams findByDataCfgOid(String dataCfgOid);

}
