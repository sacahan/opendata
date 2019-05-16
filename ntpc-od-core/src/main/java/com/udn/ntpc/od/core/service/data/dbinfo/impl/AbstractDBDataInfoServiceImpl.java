package com.udn.ntpc.od.core.service.data.dbinfo.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.dbinfo.DataSourceUtil;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.Parameter;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableField;
import com.udn.ntpc.od.model.domain.TableFields;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.batch.item.database.Order.ASCENDING;
import static org.springframework.batch.item.database.Order.DESCENDING;

@Service
@Transactional
@Slf4j
public abstract class AbstractDBDataInfoServiceImpl implements DBDataInfoService {

    private static final String DEFAULT_EQUALS = "1 = 1 ";
    private static final String MESSAGE_TABLE_FIELD = "table: %s, field: %s";

    @Autowired
    private DataSourceUtil dataSourceUtil;

    @Resource(name = "filterOperators")
    private Map<String, String> filterOperators;

    @Resource(name = "whereOperators")
    private Map<String, String> whereOperators;

    @Override
    public TableFields getTableFields(final String tableName, JdbcTemplate jdbctemplate) {
        return jdbctemplate.query(String.format("select * from %s where 2=1", getQuoteName(tableName)), new ResultSetExtractor<TableFields>() {
            @Override
            public TableFields extractData(ResultSet rs) throws SQLException {
                TableFields results = new TableFields();
                results.setTableName(tableName);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for(int i = 1; i <= columnCount; i++) {
                    TableField field = new TableField();
                    field.setId(rsmd.getColumnName(i));
                    field.setType("string");
                    field.setOriginalType(rsmd.getColumnTypeName(i));
                    results.add(field);
                }
                return results;
            }
        });
    }

    @Override
    public String genSelectedFields(TableFields tableFields) {
        StringBuilder fields = new StringBuilder("*");
        if (CollectionUtils.isNotEmpty(tableFields)) {
            fields = new StringBuilder("");
            for (TableField field : tableFields) {
                if (!StringUtils.equalsIgnoreCase("_id", field.getId())) {
                    if (StringUtils.isNotBlank(fields)) {
                        fields.append(", ");
                    }
                    fields.append(String.format("%s%s%s", getFieldKeywordWrapper()[0], field.getId(), getFieldKeywordWrapper()[1]));
                }
            }
        }
        return fields.toString();
    }
    
    @Override
    public String getQuoteName(String unquoteName) {
        Assert.notNull(unquoteName, "unquoteName can not be null");
        return getFieldKeywordWrapper()[0] + unquoteName + getFieldKeywordWrapper()[1];
    }
    
    @Override
    public Parameters genParamatersFromFilters(TableFields tableFields, String filters) {
        if (CollectionUtils.isEmpty(tableFields) || StringUtils.isBlank(filters)) {
            return Parameters.EMPTY_PARAMETERS;
        }
        return getParamatersFromFiltersEx(tableFields, filters, getFieldKeywordWrapper()[0], getFieldKeywordWrapper()[1]);
    }

    @Override
    public String genWhereConditionsFromFilters(TableFields tableFields, String filters) {
        if (CollectionUtils.isEmpty(tableFields) || StringUtils.isBlank(filters)) {
            return DEFAULT_EQUALS;
        }
        return getWhereConditionsFromFiltersEx(tableFields, filters, getFieldKeywordWrapper()[0], getFieldKeywordWrapper()[1]);
    }

    @Override
    public String genOrderByFromPageable(TableFields tableFields, Pageable p) {
        Sort sort = null;
        if (p != null && p.getSort() != null && p.getSort().iterator().hasNext()) {
            sort = p.getSort();
        }
        return genOrderByFromSort(tableFields, sort);
    }

    @Override
    public String genOrderByFromSort(TableFields tableFields, Sort sort) {
        if (CollectionUtils.isEmpty(tableFields)) {
            return null;
        }
        if (sort == null || !sort.iterator().hasNext()) {
            return getFieldKeywordWrapper()[0] + tableFields.get(0).getId() + getFieldKeywordWrapper()[1];
        }
        StringBuilder orderBy = new StringBuilder("");
        for (Order o : sort) {
            if (!tableFields.containsById(o.getProperty())) {
                throw new OpdException(
                        String.format(MESSAGE_TABLE_FIELD, tableFields.getTableName(), o.getProperty()),
                        new OpdException(ErrorCodeEnum.ERR_2080009_EXCEPTION));
            }
            if (StringUtils.isNotBlank(orderBy)) {
                orderBy.append(", ");
            }
            orderBy.append(getFieldKeywordWrapper()[0] + o.getProperty() + getFieldKeywordWrapper()[1] + " " + o.getDirection().toString());
        }
        return orderBy.toString();
    }

    @Override
    public Map<String, org.springframework.batch.item.database.Order> genBatchOrderByFromSort(TableFields tableFields, Sort sort) {
        final boolean isNeedQuote = true;
        return genBatchOrderByFromSort(tableFields, sort, isNeedQuote);
    }

    @Override
    public Map<String, org.springframework.batch.item.database.Order> genWithoutQuoteBatchOrderByFromSort(TableFields tableFields, Sort sort) {
        final boolean isNeedQuote = false;
        return genBatchOrderByFromSort(tableFields, sort, isNeedQuote);
    }
    
    private Map<String, org.springframework.batch.item.database.Order> genBatchOrderByFromSort(TableFields tableFields, Sort sort, boolean isNeedQuote) {
        Map<String, org.springframework.batch.item.database.Order> result = new LinkedHashMap<>();

        if (sort != null) {
            for (Order o : sort) {
                if (!tableFields.containsById(o.getProperty())) {
                    throw new OpdException(
                            String.format(MESSAGE_TABLE_FIELD, tableFields.getTableName(), o.getProperty()),
                            new OpdException(ErrorCodeEnum.ERR_2080009_EXCEPTION));
                }
                if (isNeedQuote) {
                    result.put(getFieldKeywordWrapper()[0] + o.getProperty() + getFieldKeywordWrapper()[1],
                            o.getDirection().equals(Direction.ASC) ? ASCENDING : DESCENDING);
                } else {
                    result.put(o.getProperty(),
                            o.getDirection().equals(Direction.ASC) ? ASCENDING : DESCENDING);
                }
            }
        }
        
        // 一定要有，不然有問題，因為如果超過一頁以上，會用 sort 當成 where 來查詢後序的資料
        if (MapUtils.isEmpty(result)) {
            for (TableField f : tableFields) {
                if (isNeedQuote) {
                    result.put(getFieldKeywordWrapper()[0] + f.getId() + getFieldKeywordWrapper()[1], ASCENDING);
                } else {
                    result.put(f.getId(), ASCENDING);
                }
            }
        }
        
        log.debug("order by " + result.toString());
        return result;
    }
    
    /**
     * String filters = "CaseID eq 8 or CaseID eq 7" ==> where CaseID = ? OR
     * CaseID = ? 至少 3 個 token 為一組
     * @param tableFields
     * @param filters
     * @param fieldWrapperStart
     * @param fieldWrapperEnd
     * @return
     */
    private Parameters getParamatersFromFiltersEx(TableFields tableFields, String filters, String fieldWrapperStart, String fieldWrapperEnd) {
        if (StringUtils.isBlank(filters)) {
            return Parameters.EMPTY_PARAMETERS;
        }
        return splitFilters(tableFields, filters, fieldWrapperStart, fieldWrapperEnd);
    }

    /**
     * String filters = "CaseID eq 8 or CaseID eq 7" ==> where CaseID = ? OR CaseID = ? <br>
     * CaseID eq 8 or CaseID eq 7 ==> <br>
     * 1. CaseID eq 8 ===> CaseID = ? OR<br>
     * 2. CaseID eq 7 ===> CaseID = ?
     * @param tableFields
     * @param filters
     * @param fieldWrapperStart
     * @param fieldWrapperEnd
     * @return
     */
    private String getWhereConditionsFromFiltersEx(TableFields tableFields, String filters, String fieldWrapperStart, String fieldWrapperEnd) {
        if (StringUtils.isBlank(filters)) {
            return DEFAULT_EQUALS;
        }
        
        String result = filters;
        
        Parameters splitedFilters = splitFilters(tableFields, result, fieldWrapperStart, fieldWrapperEnd);
        // 置換 " or " --> " OR "
/*
        for (String whereOperator: whereOperators.keySet()) {
            String whereOperatorDelim = " " + whereOperator + " ";
            if (result.contains(whereOperatorDelim)) {
                result = result.replaceAll(whereOperatorDelim, whereOperators.get(whereOperator));
            }
        }
*/
        for (Map.Entry<String, String> entry: whereOperators.entrySet()) {
            String whereOperatorDelim = " " + entry.getKey() + " ";
            if (result.contains(whereOperatorDelim)) {
                result = result.replaceAll(whereOperatorDelim, entry.getValue());
            }
        }

        for (Parameter filter: splitedFilters) {
            if (result.contains(filter.getOriginalFilter())) {
                result = result.replaceAll(filter.getOriginalFilter(), filter.getSqlCondition());
            }
        }

        return result;
    }
    
    /**
     * key: CaseID eq 8, value: "CaseID" = ?, 8
     * @param filters
     * @param fieldWrapperStart
     * @param fieldWrapperEnd
     * @return
     */
    private Parameters splitFilters(TableFields tableFields, String filters, String fieldWrapperStart, String fieldWrapperEnd) {
        Parameters result = new Parameters();
        for (String whereOperator: whereOperators.keySet()) {
            String whereOperatorDelim = " " + whereOperator + " ";
            if (filters.contains(whereOperatorDelim)) {
                String[] whereFragments = filters.split(whereOperatorDelim);
                for (String fragment: whereFragments) {
                    result.addAll(splitFilters(tableFields, fragment, fieldWrapperStart, fieldWrapperEnd));
                    filters = filters.replace(fragment, "");
                }
            }
        }        
        if (result.isEmpty()) {
            result.add(processFilterFragment(tableFields, filters, fieldWrapperStart, fieldWrapperEnd));
        }
        return result;
    }
    
    /**
     * 一次處理一個<br>
     * CaseID eq 5 --> key: "CaseID" = ?, value: 5<br>
     * 特殊情形<br>
     * CaseID eq 後面沒有值的時候, 預設為 ""
     * @param tableFields
     * @param fragment
     * @param fieldWrapperStart
     * @param fieldWrapperEnd
     * @return
     */
    private Parameter processFilterFragment(TableFields tableFields, String fragment, String fieldWrapperStart, String fieldWrapperEnd) {
/*
        for (String filterOperator: filterOperators.keySet()) {
            String filterOperatorDelim = " " + filterOperator + " ";
            if (fragment.contains(filterOperatorDelim)) {
                String[] filterOperatorTokenizers = fragment.split(filterOperatorDelim);
                if (filterOperatorTokenizers.length > 0) {
                    String fieldName = filterOperatorTokenizers[0];
//                    String fieldValue = filterOperatorTokenizers[1];
                    String fieldValue = "";
                    if (filterOperatorTokenizers.length > 1) {
                        fieldValue = filterOperatorTokenizers[1];
                    }
                    if (!tableFields.containsById(fieldName)) {
                        // where的欄位不存在, 丟出exception
                        throw new OpdException(String.format(MESSAGE_TABLE_FIELD, tableFields.getTableName(), fieldName),
                                new OpdException(ErrorCodeEnum.ERR_2080010_EXCEPTION));
                    }
                    TableField tableField = tableFields.get(tableFields.indexOfById(fieldName));
                    String sqlCondition = fieldWrapperStart + fieldName + fieldWrapperEnd + filterOperators.get(filterOperator) + "?";
                    Parameter result = new Parameter();
                    result.setName(fieldName);
                    result.setType(tableField.getType());
                    result.setValue(fieldValue);
                    result.setOriginalFilter(fragment);
                    result.setSqlCondition(sqlCondition);
                    return result;
                }
            }
        }
*/
        for (Map.Entry<String, String> entry: filterOperators.entrySet()) {
            String filterOperatorDelim = " " + entry.getKey() + " ";
            if (fragment.contains(filterOperatorDelim)) {
                String[] filterOperatorTokenizers = fragment.split(filterOperatorDelim);
                if (filterOperatorTokenizers.length > 0) {
                    String fieldName = filterOperatorTokenizers[0];
//                    String fieldValue = filterOperatorTokenizers[1];
                    String fieldValue = "";
                    if (filterOperatorTokenizers.length > 1) {
                        fieldValue = filterOperatorTokenizers[1];
                    }
                    if (!tableFields.containsById(fieldName)) {
                        // where的欄位不存在, 丟出exception
                        throw new OpdException(String.format(MESSAGE_TABLE_FIELD, tableFields.getTableName(), fieldName),
                                new OpdException(ErrorCodeEnum.ERR_2080010_EXCEPTION));
                    }
                    TableField tableField = tableFields.get(tableFields.indexOfById(fieldName));
                    String sqlCondition = fieldWrapperStart + fieldName + fieldWrapperEnd + entry.getValue() + "?";
                    Parameter result = new Parameter();
                    result.setName(fieldName);
                    result.setType(tableField.getType());
                    result.setValue(fieldValue);
                    result.setOriginalFilter(fragment);
                    result.setSqlCondition(sqlCondition);
                    return result;
                }
            }
        }
        // where的算數運算子不存在, 丟出exception
        throw new OpdException("查詢語法有誤, " + fragment, new OpdException(ErrorCodeEnum.ERR_2080011_EXCEPTION));
    }
    
    @Override
    public Boolean checkConnection(String ip, String dbName, String user, String password) {
        JdbcTemplate jdbcTemplate = getTestJdbcTemplate(ip, dbName, user, password);
        try {
            return DataSourceUtil.validationQuery(jdbcTemplate, getUrl(ip, dbName));
        } finally {
            DataSourceUtil.removeJdbcTemplate(jdbcTemplate);
            DataSourceUtil.removeDataSource(jdbcTemplate.getDataSource());
        }
    }

    @Override
    public DataSource getDataSource(String ip, String dbName, String user, String password) {
        return dataSourceUtil.getDataSource(getDriver(), getUrl(ip, dbName), user, password);
    }

    @Override
    public JdbcTemplate getJdbcTemplate(String ip, String dbName, String user, String password) {
        return dataSourceUtil.getJdbcTemplate(getDriver(), getUrl(ip, dbName), user, password);
    }

    @Override
    public JdbcTemplate getTestJdbcTemplate(String ip, String dbName, String user, String password) {
        return dataSourceUtil.getTestJdbcTemplate(getDriver(), getUrl(ip, dbName), user, password);
    }
    
    @Override
    public long dataCount(JdbcTemplate jdbcTemplate, String tableName, String where, Parameters parameters) {
        String sql = String.format("select count(*) as total from %s where %s", getQuoteName(tableName), where);
//        log.debug(sql);
        return jdbcTemplate.queryForObject(sql, parameters.getValues(), Long.class);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public DataPage<Map<String, Object>> query(JdbcTemplate jdbcTemplate, TableFields tableFields, String tableName, String filters, Pageable p) {

        String selectedFields = genSelectedFields(tableFields);

        Parameters parameters = genParamatersFromFilters(tableFields, filters);

        String where = genWhereConditionsFromFilters(tableFields, filters);

        String orderBy = genOrderByFromPageable(tableFields, p);

        return doQueryData(jdbcTemplate, tableFields, selectedFields, tableName, where, orderBy, parameters, p);
    }

    @Override
    public String[] getFieldKeywordWrapper() {
        return doGetFieldKeywordWrapper();
    }

    @Override
    public boolean clearTableData(JdbcTemplate jdbcTemplate, String tableName) {
        String sql = String.format("TRUNCATE TABLE %s", getQuoteName(tableName));
        log.debug(sql);
        jdbcTemplate.execute(sql);
        return true;
    }
    
    @Override
    public List<Map<String, Object>> groupingValues(JdbcTemplate jdbcTemplate, String tableName, String where, String groupBy, Parameters parameters) {
        StringBuilder sql = new StringBuilder();
        sql.append("select %s, count(*) as %s from %s ");
        sql.append(" where " + where);
        sql.append(" group by %s");

        String[] groupByFields = groupBy.split(",");
        StringBuilder quotedGroupBy = new StringBuilder("");
        for (String g: groupByFields) {
            if (StringUtils.isNotBlank(quotedGroupBy)) {
                quotedGroupBy.append(",");
            }
            quotedGroupBy.append(getQuoteName(g));
        }
        
        String s = String.format(sql.toString(), quotedGroupBy.toString(), getQuoteName("cnt"), getQuoteName(tableName), quotedGroupBy.toString());
        log.debug(s);
        
        return jdbcTemplate.queryForList(s, parameters.getValues());
    }
    
    protected abstract String getDriver();

    protected abstract String getUrl(String ip, String dbName);

    protected abstract String[] doGetFieldKeywordWrapper();

    protected abstract DataPage<Map<String, Object>> doQueryData(JdbcTemplate jdbcTemplate, TableFields fields,
                                                                 String selectedFields, String tableName, String where, String orderBy, Parameters parameters, Pageable p);

}
