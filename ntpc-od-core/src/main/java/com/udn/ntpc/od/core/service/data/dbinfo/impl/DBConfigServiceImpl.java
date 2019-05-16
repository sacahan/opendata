package com.udn.ntpc.od.core.service.data.dbinfo.impl;

import com.udn.ntpc.od.common.converter.StringToSortObjectConverter;
import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.core.service.cfg.DataCfgConnectionParamService;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBConfigService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.common.variables.DEFAULT_SETTINGS;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableFields;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.DBKey;
import com.udn.ntpc.od.model.common.ConnectionParam.DBKey_DB_TYPE;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 查詢資料集 raw data 
 */
@Service
@Transactional
public class DBConfigServiceImpl implements DBConfigService {
    
    /**
     * 各種資料庫 DBDataInfoService
     * spring-data.xml
     */
    @Resource(name = "dbDataInfoServices")
    private Map<String, DBDataInfoService> dbDataInfoServices;

    @Autowired
    private DataCfgService dataCfgService;
    
    @Autowired
    private StringToSortObjectConverter stringToSortObjectConverter;

    @Autowired
    private DataCfgConnectionParamService dataCfgConnectionParamService;

    @Override
    public boolean testConnection(String dataSetOid, Map<String, String> params) {
        return testConnection(dataSetOid, new DataCfgConnectionParams(params));
    }

    @Override
    public boolean testConnection(String dataSetOid, DataCfgConnectionParams params) {
        findByDataSetOid(dataSetOid);
        
        if (testConnection(params)) {
            dataCfgService.saveDataCfgConnectionParams(dataSetOid, params);
            return true;
        }
        return false;
    }

    @Override
    public boolean testConnection(DataCfgConnectionParams params) {
        DBKey_DB_TYPE dbType =  DBKey_DB_TYPE.valueOf(params.getValue(DBKey.DB_TYPE));
        String ip = params.getValue(DBKey.DB_IP);
        String dbName = params.getValue(DBKey.DB_NAME);
        String user = params.getValue(DBKey.DB_USER);
        String password = getPassword(params);
        
        DBDataInfoService dbDataInfoService = dbDataInfoServices.get(dbType.name());
        return dbDataInfoService.checkConnection(ip, dbName, user, password);
    }
    
    @Override
    public DataPage<Map<String, Object>> tableData(String dataSetOid, String tableName, String sort) {
        DataCfg dataCfg = findByDataSetOid(dataSetOid);
        DataCfgConnectionParams params = getDataCfgConnectionParams(dataCfg);

        params.setValue(DBKey.TABLE_NAME, tableName);
        params.setValue(DBKey.SORT, sort);
        params.validateParams();

        DataPage<Map<String, Object>> result = tableData(dataSetOid, params);
        
        dataCfgService.save(dataCfg);
        
        return result;
    }

    @Override
    public DataPage<Map<String, Object>> tableData(String dataSetOid, DataCfgConnectionParams params) {
        if (!testConnection(params)) {
            throw new OpdException(String.format("資料庫無法連線，請檢查資料庫連線設定！(%s)", dataSetOid));
        }
        return tableData(params);
    }

    @Override
    public DataPage<Map<String, Object>> tableData(DataCfgConnectionParams params) {
        String tableName = params.getValue(DBKey.TABLE_NAME);
        DBKey_DB_TYPE dbType =  DBKey_DB_TYPE.valueOf(params.getValue(DBKey.DB_TYPE));
        String ip = params.getValue(DBKey.DB_IP);
        String dbName = params.getValue(DBKey.DB_NAME);
        String user = params.getValue(DBKey.DB_USER);
        String password = getPassword(params);
        String filters = null;
        String sort = params.getValue(DBKey.SORT);
        
        DBDataInfoService dbDataInfoService = dbDataInfoServices.get(dbType.name());
        return queryData(dbDataInfoService, ip, dbName, user, password, tableName, filters, sort);
    }

    @Override
    public long dataCount(DataCfgConnectionParams params) {
        String tableName = params.getValue(DBKey.TABLE_NAME);
        DBKey_DB_TYPE dbType =  DBKey_DB_TYPE.valueOf(params.getValue(DBKey.DB_TYPE));
        String ip = params.getValue(DBKey.DB_IP);
        String dbName = params.getValue(DBKey.DB_NAME);
        String user = params.getValue(DBKey.DB_USER);
        String password = getPassword(params);

        DBDataInfoService dbDataInfoService = dbDataInfoServices.get(dbType.name());
        return dataCount(dbDataInfoService, ip, dbName, user, password, tableName); 
    }

    @Override
    public CustomResult<String> save(String dataSetOid, String sort, Date startTime, DataFieldCfgsDto dataFieldCfgsDto) {
        DataCfgConnectionParams params = dataCfgConnectionParamService.findByDataSetOid(dataSetOid);
        params.setValue(DBKey.SORT, sort);
        params.setValue(DBKey.START_TIME, DateFormatUtils.format(startTime, ConnectionParam.DATE_TIME_PATTERN));
        return save(dataSetOid, params, dataFieldCfgsDto);
    }

    @Override
    public CustomResult<String> save(String dataSetOid, DataCfgConnectionParams params, DataFieldCfgsDto dataFieldCfgsDto) {
        findByDataSetOid(dataSetOid);
        
        if (CollectionUtils.isEmpty(dataFieldCfgsDto))
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2070002_EXCEPTION));
        
        if (!testConnection(params)) {
            return CustomResult.result(false, "資料庫無法連線，請檢查資料庫連線設定！");
        }

        dataCfgService.saveDataCfgConnectionParams(dataSetOid, params);
        dataCfgService.saveDataFieldCfgsDto(dataSetOid, dataFieldCfgsDto);

        return CustomResult.result(true, "儲存成功");
    }
/*
    @Override
    public String getSql(String dataSetOid) {
        DataCfg dataCfg = findByDataSetOid(dataSetOid);
        DataCfgConnectionParams params = getDataCfgConnectionParams(dataCfg);
        
        return null;
    }
*/
    private DataCfg findByDataSetOid(String dataSetOid) {
        DataCfg dataCfg = dataCfgService.findByDataSetOid(dataSetOid);
        if (dataCfg == null)
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        return dataCfg;
    }
    
    private DataCfgConnectionParams getDataCfgConnectionParams(DataCfg dataCfg) {
        if (CollectionUtils.isEmpty(dataCfg.getDataCfgConnectionParams()))
            throw new OpdException(dataCfg.getDataSetOid(), new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        return new DataCfgConnectionParams(dataCfg.getDataCfgConnectionParams());
    }

    private DataPage<Map<String, Object>> queryData(DBDataInfoService dbDataInfoService,
                                                    String ip, String dbName, String user, String password, String tableName, String filters,
                                                    String sort) {
        JdbcTemplate jdbcTemplate = dbDataInfoService.getJdbcTemplate(ip, dbName, user, password);
        TableFields fields = dbDataInfoService.getTableFields(tableName, jdbcTemplate);
        Pageable p = DEFAULT_SETTINGS.DEFAULT_DATA_PAGING;
        Sort s = stringToSortObjectConverter.convert(sort);
        if (s != null) {
            p = PageRequest.of(DEFAULT_SETTINGS.DEFAULT_DATA_PAGING.getPageNumber(),
                               DEFAULT_SETTINGS.DEFAULT_DATA_PAGING.getPageSize(),
                               s);
        }
        return dbDataInfoService.query(jdbcTemplate, fields, tableName, filters, p);
    }

    private long dataCount(DBDataInfoService dbDataInfoService,
                           String ip, String dbName, String user, String password, String tableName) {
        JdbcTemplate jdbcTemplate = dbDataInfoService.getJdbcTemplate(ip, dbName, user, password);
        String where = "1 = 1";
        return dbDataInfoService.dataCount(jdbcTemplate, tableName, where, Parameters.EMPTY_PARAMETERS);
    }

    private String getPassword(DataCfgConnectionParams params) {
        DBKey_DB_TYPE dbType =  DBKey_DB_TYPE.valueOf(params.getValue(DBKey.DB_TYPE));
        return DBKey_DB_TYPE.H2.equals(dbType)? null: params.getValue(DBKey.DB_PASSWORD); // H2 for 測試用不需密碼
    }

}
