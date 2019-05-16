package com.udn.ntpc.od.core.service.data.dbinfo;

import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableFields;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface DBDataInfoService {

    /**
     * 取得實際資料庫table的所有欄位名稱
     * @param tableName table的名稱
     * @return 所有欄位名稱
     */
    TableFields getTableFields(String tableName, JdbcTemplate jdbctemplate);
    
//    String genSelectedFields(TableFields tableFields, String fieldWrapperStart, String fieldWrapperEnd);
    /**
     * 返回用 Quote 包起來的欄位, 不含 [_id]
     * @param tableFields
     * @return Quote field
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
     * 返回用 Quote 包起來的欄位
     * String filters = "CaseID eq 8 or CaseID eq 7" ==>
     *   where CaseID = ? OR CaseID = ?
     * @param tableFields
     * @param filters
     * @return 1 = 1 when filters blank
     */
    String genWhereConditionsFromFilters(TableFields tableFields, String filters);
    
    /**
     * 返回用 Quote 包起來的欄位
     * @param tableFields
     * @param p
     * @return first field when no sort, null when tableFields null
     */
    String genOrderByFromPageable(TableFields tableFields, Pageable p);

    /**
     * 返回用 Quote 包起來的欄位
     * @param tableFields
     * @param sort
     * @return first field when no sort, null when tableFields null
     */
    String genOrderByFromSort(TableFields tableFields, Sort sort);

    /**
     * 返回用 Quote 包起來的欄位
     * Gen order by for Spring Batch
     * @param tableFields
     * @param sort
     * @return
     */
    Map<String, org.springframework.batch.item.database.Order> genBatchOrderByFromSort(TableFields tableFields, Sort sort);
    
    /**
     * 返回不用 Quote 包起來的欄位
     * Gen order by for Spring Batch
     * @param tableFields
     * @param sort
     * @return
     */
    Map<String, org.springframework.batch.item.database.Order> genWithoutQuoteBatchOrderByFromSort(TableFields tableFields, Sort sort);


    Boolean checkConnection(String ip, String dbName, String user, String password);
    DataSource getDataSource(String ip, String dbName, String user, String password);
    JdbcTemplate getJdbcTemplate(String ip, String dbName, String user, String password);
    JdbcTemplate getTestJdbcTemplate(String ip, String dbName, String user, String password);
    String[] getFieldKeywordWrapper();

    /**
     * 資料筆數
     * @param jdbcTemplate
     * @param tableName
     * @param where
     * @param parameters
     * @return
     */
    long dataCount(JdbcTemplate jdbcTemplate, String tableName, String where, Parameters parameters);
    
    DataPage<Map<String, Object>> query(JdbcTemplate jdbcTemplate, TableFields tableFields, String tableName, String filters, Pageable p);

    boolean clearTableData(JdbcTemplate jdbcTemplate, String tableName);

    /**
     * 取得分群的值及筆數
     * @param jdbcTemplate
     * @param tableName
     * @param where
     * @param groupBy
     * @param parameters
     * @return group 分群值, count(*) cnt 筆數
     */
    List<Map<String, Object>> groupingValues(JdbcTemplate jdbcTemplate, String tableName, String where, String groupBy, Parameters parameters);

}
