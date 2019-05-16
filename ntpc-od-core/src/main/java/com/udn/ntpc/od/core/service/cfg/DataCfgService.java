package com.udn.ntpc.od.core.service.cfg;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;

import java.util.Map;
import java.util.Set;

public interface DataCfgService extends CustomService<DataCfg, String> {
    
    DataCfg findByDataSetOid(String dataSetOid);
    
    Set<DataFieldCfg> getDataFieldConfigs(String dataSetOid);

    DataCfg saveDataCfgConnectionParams(String dataSetOid, Map<String, String> params);
    
    DataCfg saveDataCfgConnectionParams(String dataSetOid, DataCfgConnectionParams params);
    
    DataCfg saveDataCfgConnectionParams(String dataSetOid, DataCfgAPIConnectionParams params);
    
    DataCfg saveDataFieldCfgsDto(String dataSetOid, DataFieldCfgsDto dataFieldCfgsDto);
    
    DataCfg saveDataFieldCfgs(String dataSetOid, DataFieldCfgs dataFieldCfgs);
    
    /**
     * 刪除 dataCfg 底下所有 CSV 壓縮檔
     * @param dataSetOid
     */
    void deleteAllCsvCfgZipFile(String dataSetOid);
    
    /**
     * 刪除 dataCfg 底下所有 KML 壓縮檔
     * @param dataSetOid
     */
    void deleteAllKmlCfgZipFile(String dataSetOid);

}
