package com.udn.ntpc.od.core.service.data.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.common.util.FileUtils;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.cfg.DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.cfg.DataCfgZipFileService;
import com.udn.ntpc.od.core.service.data.DataService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.data.out.DataOutService;
import com.udn.ntpc.od.core.service.set.DataSetService;
import com.udn.ntpc.od.core.service.system.CoordinateMappingService;
import com.udn.ntpc.od.core.service.system.SysParamService;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;
import com.udn.ntpc.od.model.common.Metadata;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.DataPageImpl;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableFields;
import com.udn.ntpc.od.model.set.domain.DataSet;
import com.udn.ntpc.od.model.system.domain.CoordinateMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Service
@Transactional
@Slf4j
public abstract class AbstractDataServiceImpl implements DataService {

    @Resource(name = "filterOperators")
    private Map<String, String> filterOperators;
    
    @Resource(name = "whereOperators")
    private Map<String, String> whereOperators;
/*
    @Autowired
    private DataRepository dataRepository;
*/    
    @Autowired
    private DataSetService dataSetService;
    
    @Autowired
    private DataCfgService dataCfgService;
    
    @Autowired
    private DataCfgZipFileService dataCfgZipFileService;

    @Autowired
    private DataOutService dataOutService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
/*    
    @Autowired
    private DataInUtils dataInUtils;
*/    
    @Autowired
    private SysParamService sysParamService;
    
    @Autowired
    private CoordinateMappingService coordinateMappingService;

    @Override
    public TableFields getTableFieldsByDataCfgOid(String dataCfgOid) {
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
/* 一律都從 DataFieldCfgs 取，因為有 Address
        TableFields result = dataCfgTableInfoService.getTableFieldsByDataCfgOid(dataCfgOid);
        if (!result.isEmpty())
            return result;
*/        
        TableFields result = dataCfgTableInfoService.getTableFieldsFromDataFieldCfgs(dataCfgOid);
        if (!result.isEmpty())
            return result;

        if (StringUtils.isNotBlank(result.getTableName()))
            return getDBDataInfoService().getTableFields(result.getTableName(), jdbcTemplate);

        return TableFields.EMPTY_FIELDS;
    }

    @Override
    public TableFields getBatchTableFieldsByDataCfgOid(String dataCfgOid) {
        TableFields result = getTableFieldsByDataCfgOid(dataCfgOid);
        if (result.hasAddressField()) {
            result.removeCoordinateDataFields();
        }
/*
        if (result.hasIdField()) {
            result.removeIdField();
        }
*/
        return result;
    }
    
    @Override
    public String genSelectedFields(TableFields tableFields) {
    	return getDBDataInfoService().genSelectedFields(tableFields);
    }

    @Override
    public String getQuoteName(String tableName) {
        return getDBDataInfoService().getQuoteName(tableName);
    }
    
    @Override
    public Parameters genParamatersFromFilters(TableFields tableFields, String filters) {
    	return getDBDataInfoService().genParamatersFromFilters(tableFields, filters);
    }

    @Override
    public String genWhereConditionsFromFilters(TableFields tableFields, String filters) {
    	return getDBDataInfoService().genWhereConditionsFromFilters(tableFields, filters);
    }
    
    @Override
    public String genOrderByFromPageable(TableFields tableFields, Pageable p) {
    	return getDBDataInfoService().genOrderByFromPageable(tableFields, p);
    }
    
    @Override
    public String genOrderByFromSort(TableFields tableFields, Sort sort) {
    	return getDBDataInfoService().genOrderByFromSort(tableFields, sort);
    }
    
    @Override
    public Map<String, org.springframework.batch.item.database.Order> genBatchOrderByFromSort(TableFields tableFields, Sort sort) {
        return getDBDataInfoService().genBatchOrderByFromSort(tableFields, sort);
    }
    
    @Override
    public Map<String, org.springframework.batch.item.database.Order> genWithoutQuoteBatchOrderByFromSort(TableFields tableFields, Sort sort) {
        return getDBDataInfoService().genWithoutQuoteBatchOrderByFromSort(tableFields, sort);
    }

    @Override
    @Transactional(isolation= Isolation.READ_UNCOMMITTED)
    public DataPage<Map<String, Object>> queryByDataSetOid(String dataSetOid, Pageable p) {
        String filters = null;
        return queryByDataSetOid(dataSetOid, filters, p);
    }
    
    @Override
    @Transactional(isolation= Isolation.READ_UNCOMMITTED)
    public DataPage<Map<String, Object>> queryByDataSetOid(String dataSetOid, String filters, Pageable p) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);

        DataSet dataSet = dataCfg.getDataSet();
        if (dataSet == null)
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080000_EXCEPTION));
        
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
        return dataCfgTableInfoService.queryData(dataCfg.getDataCfgTableInfo(), filters, p);
    }

    @Override
    @Transactional(isolation= Isolation.READ_UNCOMMITTED)
    public DataPage<Map<String, Object>> queryPublicDataByDataSetOid(String dataSetOid, String filters, Pageable p) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        // dataSet 與 dataCfg 必須同時滿足
        // 有效 && 上架&& 公開
        DataSet dataSet = dataCfg.getDataSet();
        if (dataSet == null)
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080000_EXCEPTION));
        // dataSet 與 dataCfg 必須同時滿足
        // 有效 && 上架&& 公開
        boolean isSatisfy = 
                (dataSet.isActive() && dataSet.isEnable() && dataSet.isPublic()) &&
                (dataCfg.isActive() && dataCfg.isEnable() && dataCfg.isPublic());
        // 若不滿足條件，則直接回傳 null 代表取無資料。
        if (!isSatisfy) {
            return new DataPageImpl<>(new ArrayList<Map<String, Object>>(), p, 0);
        }
        //檢查是否為非結構化資料
        if (!dataCfg.isStructured()) {
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080005_EXCEPTION));
        }
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
        return dataCfgTableInfoService.queryData(dataCfg.getDataCfgTableInfo(), filters, p);
    }
    
    @Override
    public DataCfgTableInfo createTableIfNotExists(String dataCfgOid) {
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
        return dataCfgTableInfoService.saveIfNotExists(dataCfgOid);
    }
    
    @Override
    public boolean clearTableData(String dataCfgOid) {
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
        return dataCfgTableInfoService.clearTableData(dataCfgOid);
    }

    @Override
    public long dataCountByDataSetOid(String dataSetOid, String filters) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        DataCfgTableInfoService dataCfgTableInfoService = getDataCfgTableInfoService();
        return dataCfgTableInfoService.dataCount(dataCfg.getOid(), filters);
    }
    
    @Override
    public File genCsvDataToZipFile(String dataSetOid, String filters) throws IOException {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        String baseName = dataCfg.getName();
        File tmpDataFile = File.createTempFile(dataSetOid + "-" + baseName + "_", ".csv");

        try {
            try (OutputStream output = IOUtils.buffer(new FileOutputStream(tmpDataFile));) {
                dataOutService.outputCsvData(dataSetOid, filters, output);
            }
            if (tmpDataFile.length() == 0) {
                throw new OpdException("檔案沒有內容");
            }
            File zipFile = File.createTempFile(baseName, ".zip");
            FileUtils.zip(tmpDataFile, zipFile);
            return zipFile;
        } finally {
            FileUtils.deleteQuietly(tmpDataFile);
        }
    }
    
    @Override
    public DataCfgZipFile exportCsvZipFile(String dataSetOid, String filters) throws IOException {
        log.debug("Export all data to csv and zip...");
        try {
            File zipCsvFile = genCsvDataToZipFile(dataSetOid, filters);
            try {
                // 怕同類型的檔案會重複，所以全刪了
                dataCfgService.deleteAllCsvCfgZipFile(dataSetOid);
                return dataCfgZipFileService.updateZipFile(dataSetOid, zipCsvFile, "csv");
            } finally {
                FileUtils.deleteQuietly(zipCsvFile);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
    
    @Override
    public File genKmlDataToZipFile(String dataSetOid, String filters) throws IOException {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        String baseName = dataCfg.getName();
        File tmpDataFile = File.createTempFile(dataSetOid + "-" + baseName + "_", ".kml");
        try {
            try (OutputStream output = IOUtils.buffer(new FileOutputStream(tmpDataFile));) {
                dataOutService.outputKmlData(dataSetOid, filters, output);
            }
            if (tmpDataFile.length() == 0) {
                throw new OpdException("檔案沒有內容");
            }
            File zipFile = File.createTempFile(baseName, ".zip");
            FileUtils.zip(tmpDataFile, zipFile);
            return zipFile;
        } finally {
            FileUtils.deleteQuietly(tmpDataFile);
        }
    }

    @Override
    public DataCfgZipFile exportKmlZipFile(String dataSetOid, String filters) throws IOException {
        DataSet dataSet = findDataSetOid(dataSetOid);
        if (dataSet.isKml()) {
            log.debug("Export all data to kml and zip...");
            try {
                File zipKmlFile = genKmlDataToZipFile(dataSetOid, filters);
                // 怕同類型的檔案會重複，所以全刪了
                dataCfgService.deleteAllKmlCfgZipFile(dataSetOid);
                try {
                    return dataCfgZipFileService.updateZipFile(dataSetOid, zipKmlFile, "kml");
                } finally {
                    FileUtils.deleteQuietly(zipKmlFile);
                }
            } catch(IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }
    
    @Override
    public CoordinateMapping updateCoordinateMapping(String dataSetOid) {
        DataSet dataSet = findDataSetOid(dataSetOid);
        if (dataSet.isKml()) {
            log.debug("Address field to CoordinateMapping...");
            DataFieldCfgs fields = (DataFieldCfgs) dataSet.getDataCfg().getDataFieldCfgs();
            if (fields.hasAddressField()) {
                String dataCfgOid = dataSet.getDataCfg().getOid();
                CoordinateMapping coordinateMapping = coordinateMappingService.getOne(dataCfgOid);
                if (coordinateMapping == null) {
                    coordinateMapping = new CoordinateMapping();
                }
                DataFieldCfg address = fields.getAddressField();
                coordinateMapping.setFieldAddr(address.getFieldName());
                coordinateMapping.setFieldName("");
                List<String> coordinateMappingFieldNames = Arrays.asList(sysParamService.getCoordinateMappingFieldNames());
                coordinateMapping.setDataCfgOid(dataCfgOid);
                if (CollectionUtils.isNotEmpty(fields)) {
                    // coordinateMappingFieldNames 預設用第一個欄位
                    coordinateMapping.setFieldName(fields.firstField().getFieldName());
                    for (DataFieldCfg field: fields) {
                        for (String fieldName: coordinateMappingFieldNames) {
                            if (field.getFieldName().toLowerCase().contains(fieldName.toLowerCase())) {
                                coordinateMapping.setFieldName(field.getFieldName());
                                break;
                            }
                        }
                    }
                }
                coordinateMappingService.save(coordinateMapping);
                return coordinateMapping;
            }
        }
        return null;
    }
    
    @Override
    public List<Map<String, Object>> getGroupingValuesByDataSetOid(String dataSetOid, String filters) {
        DataSet dataSet = findDataSetOid(dataSetOid);
        if (!dataSet.isParent()) {
            return Collections.emptyList();
        }
        String dataCfgOid = dataSet.getDataCfg().getOid();
        return getDataCfgTableInfoService().getGroupingValues(dataCfgOid, filters);
    }
    
	@Override
    public DataSet newChildDataSetByGroupingValue(DataSet parentDataSet, String groupingValue) {
	    Assert.notNull(groupingValue, "groupingValue null");
	    
	    String identifier = sysParamService.genChildSerialNumber();
	    
        DataSet childDataSet = parentDataSet.cloneEntity();
//        childDataSet.setDescription(childDataSet.getDescription() + "-" + groupingValue);
//        childDataSet.setName(childDataSet.getName() + "-" + groupingValue);
        childDataSet.setDescription(parentDataSet.getDescription() + "-" + groupingValue);
        childDataSet.setName(parentDataSet.getName() + "-" + groupingValue);
        childDataSet.setParentDataSet(parentDataSet);
        Metadata.setIdentifier(childDataSet, identifier);
        dataSetService.save(childDataSet);
        
        DataCfg childDataCfg = parentDataSet.getDataCfg().cloneEntity();
//        childDataCfg.setDescription(childDataCfg.getDescription() + "-" + groupingValue);
//        childDataCfg.setName(childDataCfg.getName() + "-" + groupingValue);
        childDataCfg.setDescription(childDataSet.getDescription());
        childDataCfg.setName(childDataSet.getName());
        childDataCfg.setDataSet(childDataSet);
        Metadata.setResourceId(childDataCfg, sysParamService.genResourceId(identifier));
        Metadata.setSubResourceIds(childDataCfg, sysParamService.genSubResourceIds(identifier));
        Metadata.setGroupingValue(groupingValue, childDataCfg);
        dataCfgService.save(childDataCfg);
        
        return childDataSet;
    }

	@Override
	public void updateChildDataSetFromParentDataSet(DataSet parentDataSet, DataSet childDataSet, String groupingValue) {
	    childDataSet.getDataCfg().copyFrom(parentDataSet.getDataCfg());
//        childDataSet.getDataCfg().setDescription(childDataSet.getDataCfg().getDescription() + "-" + groupingValue);
//        childDataSet.getDataCfg().setName(childDataSet.getDataCfg().getName() + "-" + groupingValue);
	    childDataSet.getDataCfg().setDescription(parentDataSet.getDescription() + "-" + groupingValue);
	    childDataSet.getDataCfg().setName(parentDataSet.getName() + "-" + groupingValue);
	    Metadata.setGroupingValue(groupingValue, childDataSet.getDataCfg());
	    dataCfgService.save(childDataSet.getDataCfg());

        childDataSet.copyFrom(parentDataSet);
//        childDataSet.setDescription(childDataSet.getDescription() + "-" + groupingValue);
//        childDataSet.setName(childDataSet.getName() + "-" + groupingValue);
        childDataSet.setDescription(parentDataSet.getDescription() + "-" + groupingValue);
        childDataSet.setName(parentDataSet.getName() + "-" + groupingValue);
        dataSetService.save(childDataSet);
	}
	
    @Override
    public DataSet getChildDataSetByGroupingValue(DataSet parentDataSet, String groupingValue) {
        Assert.notNull(groupingValue, "groupingValue null");

        for (DataSet childDataSet: parentDataSet.getChildDataSets()) {
            String childGroupingValue = Metadata.getGroupingValue(childDataSet.getDataCfg());
//            if (StringUtils.isNotBlank(childGroupingValue) && StringUtils.equals(groupingValue, childGroupingValue))
            // 空字串也接受
            if (StringUtils.equals(groupingValue, childGroupingValue)) {
                return childDataSet;
            }
        }

        return null;
    }
    
    @Override
    public Date updateDataSetUpdateTime(String dataSetOid) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        Date updateTime = new Date();
        dataCfg.setUpdateTime(updateTime);
        dataCfgService.save(dataCfg);
        return updateTime;
    }
/*
    @Override
    public List<DataSet> groupingDataSets(String parentDataSetOid, String filters) {
        DataSet parentDataSet = findDataSetOid(parentDataSetOid);
        
        for (Iterator<DataSet> childDataSets = parentDataSet.getChildDataSets().iterator(); childDataSets.hasNext();) {
            DataSet childDataSet = childDataSets.next();
            childDataSet.setPublic(false);
            dataSetService.save(childDataSet);
        }
        
        if (!parentDataSet.isParent()) {
            return Collections.emptyList();
        }
        
        List<Map<String, Object>> groupingValues = getGroupingValuesByDataSetOid(parentDataSetOid, filters);
        List<DataSet> results = new ArrayList<DataSet>();
        for (Map<String, Object> groupingValue: groupingValues) {
            String groupingValueString = dataInUtils.genGroupingValueString(groupingValue);
            DataSet childDataSet = getChildDataSetByGroupingValue(parentDataSet, groupingValueString);
            if (childDataSet == null) {
                childDataSet = newChildDataSetByGroupingValue(parentDataSet, groupingValueString);
            } else {
                updateChildDataSetFromParentDataSet(parentDataSet, childDataSet, groupingValueString); // 同步資料集資料，parent --> child
            }
            childDataSet.setPublic(false); // 子資料集預設不開放, 等到資料轉入後才開放
            dataSetService.save(childDataSet);
            results.add(childDataSet);
        }
        
        return results;
    }
*/
    protected abstract DataCfgTableInfoService getDataCfgTableInfoService();
	protected abstract DBDataInfoService getDBDataInfoService();

    private DataCfg findDataCfgByDataSetOid(String dataSetOid) {
        DataCfg dataCfg = dataCfgService.findByDataSetOid(dataSetOid);
        if (dataCfg == null)
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        return dataCfg;
    }

    private DataSet findDataSetOid(String dataSetOid) {
        DataSet dataSet = dataSetService.getOne(dataSetOid);
        if (dataSet == null)
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080000_EXCEPTION));
        return dataSet;
    }

}
