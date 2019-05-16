package com.udn.ntpc.od.model.cfg.dto;

import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class DataCfgAPIConnectionParams extends DataCfgConnectionParams {

    private static final long serialVersionUID = -770183654690728232L;

    /**
     * 轉換成前端 ValueObject 使用
     */
    public DataCfgAPIConnectionParams() {};
    public DataCfgAPIConnectionParams(List<DataCfgConnectionParam> params) {
    	super(params);
    }
    public DataCfgAPIConnectionParams(Map<String, String> params) {
        fromMap(params);
    }

    public void fromMap(Map<String, String> source) {
     	clear();
    	defaultParams();        
    	for (String paramKey: source.keySet()) {
    		if (!APIKey.resovleByKey(paramKey).equals(APIKey.DATA_CFG_OID)) {
    			String value = source.get(paramKey);
    			this.setValue(APIKey.resovleByKey(paramKey), value);
    		}
    	}
    }
    
    public static DataCfgAPIConnectionParams defaultInstance(boolean genDefaultParams) {
    	DataCfgAPIConnectionParams result = new DataCfgAPIConnectionParams();
        if (genDefaultParams) {
            result.defaultParams();
        }
        return result;
    }
    
    protected void defaultParams() {
        DataCfg dataCfg = null;
        defaultParams(dataCfg);
    }
    
    protected void defaultParams(DataCfg dataCfg) {
        for (APIKey param: ConnectionParam.APIKey.values()) {
            add(new DataCfgConnectionParam(dataCfg, param.getKey(), param.getDefaultValue()));
        }
    }

}
