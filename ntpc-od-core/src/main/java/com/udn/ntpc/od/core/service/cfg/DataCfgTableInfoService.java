package com.udn.ntpc.od.core.service.cfg;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.TableFields;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DataCfgTableInfoService extends CustomService<DataCfgTableInfo, String> {

    /**
     * get table fields from tableSchema
     * @param tableName
     * @return
     * @Deprecated
     */
//    @Deprecated
//    TableFields getTableFields(String tableName);
    
    /**
     * get table fields from DataCfgTableInfo.tableSchema, 含 _id 欄位
     * @param dataCfgOid
     * @return
     */
    TableFields getTableFieldsByDataCfgOid(String dataCfgOid);

    /**
     * get table fields from DataCfgTableInfo.tableSchema, 含 _id 欄位
     * @param dataCfgTableInfo
     * @return
     */
    TableFields getTableFields(DataCfgTableInfo dataCfgTableInfo);

    /**
     * get table fields from DataFieldCfgs, 不含 _id 欄位
     * @param dataCfgOid
     * @return
     */
    TableFields getTableFieldsFromDataFieldCfgs(String dataCfgOid);
    
    /**
     * Find DataCfgTableInfo by tableName
     */
//    DataCfgTableInfo findByTableName(String tableName);
    DataCfgTableInfo findByDataCfgOid(String dataCfgOid);

    /**
     * query data from DataCfgTableInfo.tableName
     * @param dataCfgTableInfo
     * @param filters
     * @param p
     * @return
     */
    DataPage<Map<String, Object>> queryData(DataCfgTableInfo dataCfgTableInfo, String filters, Pageable p);
    
    /**
     * 儲存欄位 schema 及對應的 data table
     * @param dataCfgOid
     * @return DataCfgTableInfo quer DataCfgTableInfo if not exists create new DataCfgTableInfo & data table
     */
    DataCfgTableInfo saveIfNotExists(String dataCfgOid);

    boolean clearTableData(String dataCfgOid);
    
    long dataCount(String dataCfgOid, String filters);

    /**
     * 取得分群的值及筆數
     * @param dataCfgOid
     * @return
     */
    List<Map<String, Object>> getGroupingValues(String dataCfgOid, String filters);

}
