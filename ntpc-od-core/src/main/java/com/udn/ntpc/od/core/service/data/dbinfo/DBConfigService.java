package com.udn.ntpc.od.core.service.data.dbinfo;

import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.domain.DataPage;

import java.util.Date;
import java.util.Map;

public interface DBConfigService {

    /**
     * 測試連線，測試沒有問題後儲存連線資訊
     * @param dataSetOid
     * @param params
     * @return
     */
    boolean testConnection(String dataSetOid, Map<String, String> params);
    
    /**
     * 測試連線，測試沒有問題後儲存連線資訊
     * @param dataSetOid
     * @param params
     * @return
     */
    boolean testConnection(String dataSetOid, DataCfgConnectionParams params);

    /**
     * 測試連線
     * @param params
     * @return
     */
    boolean testConnection(DataCfgConnectionParams params);
    
    /**
     * 取得 table 內容, 查詢成功後不論有沒有資料皆會儲存 tableName 及 sort
     * @param dataSetOid
     * @param tableName
     * @return 前 30 筆及欄位資訊
     */
    DataPage<Map<String, Object>> tableData(String dataSetOid, String tableName, String sort);

    /**
     * 取得 table 內容, 查詢成功後，不會儲存 tableName 及 sort
     * @param dataSetOid
     * @param params
     * @return
     */
    DataPage<Map<String, Object>> tableData(String dataSetOid, DataCfgConnectionParams params);
    
    /**
     * 取得 table 內容, 查詢成功後，不會儲存 tableName 及 sort
     * @param params
     * @return
     */
    DataPage<Map<String, Object>> tableData(DataCfgConnectionParams params);
    
    /**
     * 資料筆數
     * @param params
     * @return
     */
    long dataCount(DataCfgConnectionParams params);
    
//    CustomResult<String> save(String dataSetOid, Map<String, String> params, DataFieldCfgs dataFieldCfgs);
    
    CustomResult<String> save(String dataSetOid, String sort, Date startTime, DataFieldCfgsDto dataFieldCfgsDto);
    
    CustomResult<String> save(String dataSetOid, DataCfgConnectionParams params, DataFieldCfgsDto dataFieldCfgsDto);
    
//    CustomResult<String> save(String dataSetOid, DataCfgConnectionParams params, DataFieldCfgs dataFieldCfgs);

//    CustomResult<String> saveParams(String dataSetOid, DataCfgConnectionParams params);

//    CustomResult<String> saveFieldConfigs(String dataSetOid, DataFieldCfgsDto dataFieldCfgsDto);
/*
    *//**
     * 依據設定產生 select sql
     * @param dataSetOid
     * @return
     *//*
    String getSql(String dataSetOid);
*/
}
