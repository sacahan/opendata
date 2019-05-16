package com.udn.ntpc.od.core.service.data;

import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableFields;
import com.udn.ntpc.od.model.set.domain.DataSet;
import com.udn.ntpc.od.model.system.domain.CoordinateMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 資料集資料服務
 *
 */
public interface DataService {

//    List<String> getTableFieldNames(String tableName);
//    TableFields getTableFields(String tableName);
    /**
     * 取得table的所有欄位名稱, 不含 _id 欄位<br>
     * <del>1st DataCfgTableInfo.tableSchema</del><br>
     * 2st DataFieldCfgs<br>
     * 3st Database table<br>
     * @param dataCfgOid
     * @return 所有欄位名稱
     */
    TableFields getTableFieldsByDataCfgOid(String dataCfgOid);
    
    /**
     * Gen fields for Spring Batch, ignore CoordinateDataFields {@link com.udn.ntpc.od.model.common.ADDR_FIELDS}
     * @param dataCfgOid
     * @return
     */
    TableFields getBatchTableFieldsByDataCfgOid(String dataCfgOid);

//    String genSelectedFields(TableFields tableFields);
    
//    String genSelectedFields(TableFields tableFields, String fieldWrapperStart, String fieldWrapperEnd);
    /**
     * 返回用 Quote 包起來的 fieldName<br>
     * 如: SQL Server [xx]
     * @param tableFields
     * @return
     */
    String genSelectedFields(TableFields tableFields);

    /**
     * 返回用 Quote 包起來的 unquoteName<br>
     * 如: SQL Server [unquoteName]
     * @param unquoteName
     * @return
     */
    String getQuoteName(String unquoteName);

    /**
     * String filters = "CaseID eq 8 or CaseID eq 7" ==>
     *   where CaseID = ? OR CaseID = ?
     * @param tableFields
     * @param filters
     * @return empty list when filters blank
     */
    Parameters genParamatersFromFilters(TableFields tableFields, String filters);

    /**
     * String filters = "CaseID eq 8 or CaseID eq 7" ==>
     *   where CaseID = ? OR CaseID = ?
     * @param tableFields
     * @param filters
     * @return 1 = 1 when filters blank
     */
//    String genWhereConditionsFromFilters(TableFields tableFields, String filters, String fieldWrapperStart, String fieldWrapperEnd);
    String genWhereConditionsFromFilters(TableFields tableFields, String filters);
    
    /**
     * 
     * @param tableFields
     * @param p
     * @return first field when no sort, null when tableFields null
     */
//    String genOrderByFromPageable(TableFields tableFields, Pageable p, String fieldWrapperStart, String fieldWrapperEnd);
    String genOrderByFromPageable(TableFields tableFields, Pageable p);
    /**
     * 
     * @param tableFields
     * @param sort
     * @return first field when no sort, null when tableFields null
     */
//    String genOrderByFromSort(TableFields tableFields, Sort sort, String fieldWrapperStart, String fieldWrapperEnd);
    String genOrderByFromSort(TableFields tableFields, Sort sort);
    /**
     * Gen order by for Spring Batch
     * @param tableFields
     * @param sort
     * @return
     */
    Map<String, org.springframework.batch.item.database.Order> genBatchOrderByFromSort(TableFields tableFields, Sort sort);

    Map<String, org.springframework.batch.item.database.Order> genWithoutQuoteBatchOrderByFromSort(TableFields tableFields, Sort sort);

    DataPage<Map<String, Object>> queryByDataSetOid(String dataSetOid, Pageable p);
    DataPage<Map<String, Object>> queryByDataSetOid(String dataSetOid, String filters, Pageable p);
    /**
     * 查詢有開放資料集的資料, enable & public & active
     * @param dataSetOid
     * @param filters
     * @param p
     * @return
     */
    DataPage<Map<String, Object>> queryPublicDataByDataSetOid(String dataSetOid, String filters, Pageable p);

    /**
     * crate data table if not exists
     * @param dataCfgOid
     */
    DataCfgTableInfo createTableIfNotExists(String dataCfgOid);
    
    /**
     * 清除資料
     * @param dataCfgOid
     * @return
     */
    boolean clearTableData(String dataCfgOid);

    /**
     * 計算資料筆數
     * @param dataSetOid
     * @param filters
     * @return
     */
    long dataCountByDataSetOid(String dataSetOid, String filters);
    
    /**
     * 將資料集資料轉成 CSV 並 ZIP
     * @param dataSetOid
     * @param filters
     * @return
     * @throws java.io.IOException
     */
    File genCsvDataToZipFile(String dataSetOid, String filters) throws IOException;

    DataCfgZipFile exportCsvZipFile(String dataSetOid, String filters) throws IOException;
    /**
     * 將資料集資料轉成 KML 並 ZIP
     * @param dataSetOid
     * @param filters
     * @return
     * @throws java.io.IOException
     */
    File genKmlDataToZipFile(String dataSetOid, String filters) throws IOException;

    DataCfgZipFile exportKmlZipFile(String dataSetOid, String filters) throws IOException;
    
    /**
     * 將地址欄位儲存
     * @param dataSetOid
     * @return
     */
    CoordinateMapping updateCoordinateMapping(String dataSetOid);

    /**
     * 取得分群的值及筆數
     * @param dataSetOid
     * @param filters
     * @return group 分群值, count(*) cnt 筆數
     */
    List<Map<String, Object>> getGroupingValuesByDataSetOid(String dataSetOid, String filters);
    
    DataSet newChildDataSetByGroupingValue(DataSet parentDataSet, String groupingValue);
    
    void updateChildDataSetFromParentDataSet(DataSet parentDataSet, DataSet childDataSet, String groupingValueString);

    DataSet getChildDataSetByGroupingValue(DataSet parentDataSet, String groupingValue);

    /**
     * 依據分群條件複製 DataSet, 並把原本的 Child DataSet 設為不開放
     * @param parentDataSetOid
     * @return
     */
//    List<DataSet> groupingDataSets(String parentDataSetOid, String filters);
    
    /**
     * 更新資料集更新時間
     * @param dataSetOid
     * @return
     */
    Date updateDataSetUpdateTime(String dataSetOid);

}
