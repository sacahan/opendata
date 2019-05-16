package com.udn.ntpc.od.core.service.cfg.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.cfg.DataCfgTableInfoService;
import com.udn.ntpc.od.core.service.cfg.DataFieldCfgService;
import com.udn.ntpc.od.core.service.data.dbinfo.DBDataInfoService;
import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.cfg.repository.DataCfgTableInfoRepository;
import com.udn.ntpc.od.model.common.Metadata;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.DataPageImpl;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableField;
import com.udn.ntpc.od.model.domain.TableFields;
import com.udn.ntpc.od.model.domain.TableSchema;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class AbstractDataCfgTableInfoServiceImpl<T, ID extends Serializable>
        extends AbstractCustomServiceImpl<DataCfgTableInfo, String> implements DataCfgTableInfoService {
    protected static final Log log = LogFactory.getLog(AbstractDataCfgTableInfoServiceImpl.class);
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected DataCfgService dataCfgService;
    
    @Autowired
    protected DataFieldCfgService dataFieldCfgService;
    
    private Object lock = new Object();

    @Autowired
    private DataCfgTableInfoRepository repository;

    @Override
    public TableFields getTableFieldsByDataCfgOid(String dataCfgOid) {
        DataCfgTableInfo dataCfgTableInfo = findByDataCfgOid(dataCfgOid);
        return getTableFields(dataCfgTableInfo);
    }
    
    @Override
    public TableFields getTableFields(DataCfgTableInfo dataCfgTableInfo) {
        TableSchema schema = getTableSchema(dataCfgTableInfo);
        if (schema == null)
            return TableFields.EMPTY_FIELDS;
        return schema.getFields();
    }
    
    @Override
    public TableFields getTableFieldsFromDataFieldCfgs(String dataCfgOid) {
        TableSchema schema = genTableSchema(dataCfgOid);
        return schema.getFields();
    }

    @Override
    public DataCfgTableInfo findByDataCfgOid(String dataCfgOid) {
        return repository.findByDataCfgOid(dataCfgOid);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public DataPage<Map<String, Object>> queryData(DataCfgTableInfo dataCfgTableInfo, String filters, Pageable p) {
        if (dataCfgTableInfo == null)
            return new DataPageImpl<>(new ArrayList<Map<String, Object>>(), p, 0);
        
        DBDataInfoService dbDataInfoService = getDBDataInfoService();
        
        TableFields tableFields = getTableFields(dataCfgTableInfo);
        
        DataPage<Map<String, Object>> results = dbDataInfoService.query(jdbcTemplate, tableFields, dataCfgTableInfo.getTableName(), filters, p);
        
        DataFieldCfgsDto dataFieldCfgs = new DataFieldCfgsDto();
        dataFieldCfgs.copyFrom(dataCfgTableInfo.getDataCfg().getDataFieldCfgs());

        return new DataPageImpl<>(results.getContent(), p, results.getTotalElements(), results.getFields(), dataFieldCfgs);
    }

    @Override
    public DataCfgTableInfo saveIfNotExists(String dataCfgOid) {
        synchronized (lock) {
            DataCfgTableInfo dataCfgTableInfo = repository.findByDataCfgOid(dataCfgOid);
            TableSchema tableSchema = genTableSchema(dataCfgOid);
            if (dataCfgTableInfo == null) {
                dataCfgTableInfo = new DataCfgTableInfo();
                DataCfg dataCfg = dataCfgService.getOne(dataCfgOid);
                dataCfgTableInfo.setDataCfg(dataCfg);
                save(dataCfgTableInfo); // save to get oid for table name
                String tableName = String.format("data_%s", dataCfgTableInfo.getOid());
                dataCfgTableInfo.setTableName(tableName);
                tableSchema.setTableName(tableName);
                createTable(entityManager, tableSchema);
            }
            if (StringUtils.isBlank(dataCfgTableInfo.getTableName())) {
                String tableName = String.format("data_%s", dataCfgTableInfo.getOid());
                dataCfgTableInfo.setTableName(tableName);
            }
            if (StringUtils.isBlank(tableSchema.getTableName())) {
                tableSchema.setTableName(dataCfgTableInfo.getTableName());
            }
            dataCfgTableInfo.setTableSchema(tableSchema.toJsonString()); //欄位有可能會變更
//            saveAndFlush(dataCfgTableInfo);
            save(dataCfgTableInfo);
            verifyTable(dataCfgTableInfo);
            return dataCfgTableInfo;
        }
    }
    
    @Override
    public boolean clearTableData(String dataCfgOid) {
        DataCfgTableInfo dataCfgTableInfo = repository.findByDataCfgOid(dataCfgOid);
        String tableName = dataCfgTableInfo.getTableName();
        return getDBDataInfoService().clearTableData(jdbcTemplate, tableName);
    }
    
    @Override
    public long dataCount(String dataCfgOid, String filters) {
        DataCfgTableInfo dataCfgTableInfo = repository.findByDataCfgOid(dataCfgOid);
        return dataCount(dataCfgTableInfo, filters);
    }
    
    protected long dataCount(DataCfgTableInfo dataCfgTableInfo, String filters) {
        DBDataInfoService dbDataInfoService = getDBDataInfoService();
        
        if (dataCfgTableInfo == null)
            return 0;
        
        TableFields tableFields = getTableFields(dataCfgTableInfo);
        
        Parameters parameters = dbDataInfoService.genParamatersFromFilters(tableFields, filters);
        
        String where = dbDataInfoService.genWhereConditionsFromFilters(tableFields, filters);
        
        return dbDataInfoService.dataCount(jdbcTemplate, dataCfgTableInfo.getTableName(), where, parameters);
    }

    @Override
    public List<Map<String, Object>> getGroupingValues(String dataCfgOid, String filters) {
        DataCfgTableInfo dataCfgTableInfo = repository.findByDataCfgOid(dataCfgOid);
        return getGroupingValues(dataCfgTableInfo, filters);
    }

    protected List<Map<String, Object>> getGroupingValues(DataCfgTableInfo dataCfgTableInfo, String filters) {
        DBDataInfoService dbDataInfoService = getDBDataInfoService();
        
        if (dataCfgTableInfo == null)
            return Collections.emptyList();
        
        TableFields tableFields = getTableFields(dataCfgTableInfo);
        Parameters parameters = dbDataInfoService.genParamatersFromFilters(tableFields, filters);
        String where = dbDataInfoService.genWhereConditionsFromFilters(tableFields, filters);
        
        DataCfg dataCfg = dataCfgTableInfo.getDataCfg();
        String groupingFields = Metadata.getGroupingFields(dataCfg);
        
        return dbDataInfoService.groupingValues(jdbcTemplate, dataCfgTableInfo.getTableName(), where, groupingFields, parameters);
    }
    
    protected abstract DBDataInfoService getDBDataInfoService();

    protected String genSelectedFields(DataCfgTableInfo dataCfgTableInfo) {
        DBDataInfoService dbDataInfoService = getDBDataInfoService();
        TableFields tableFields = getTableFields(dataCfgTableInfo);
        
        return dbDataInfoService.genSelectedFields(tableFields);
    }

    protected DataFieldCfgs getDataFieldCfgs(String dataCfgOid) {
        DataFieldCfgs dataFieldCfgs = dataFieldCfgService.findByDataCfgOid(dataCfgOid);
        if (CollectionUtils.isEmpty(dataFieldCfgs))
            throw new OpdException(dataCfgOid, new OpdException("未設定欄位定義"));
        return dataFieldCfgs;
    }

    /**
     * gen TableSchema from DataFieldCfgs, 不含 _id 欄位
     * @param dataCfgOid
     * @return TableSchema if no DataFieldCfgs then TableSchema.fields empty
     */
    private TableSchema genTableSchema(String dataCfgOid) {
        DataCfgTableInfo dataCfgTableInfo = findByDataCfgOid(dataCfgOid);
        DataFieldCfgs dataFieldCfgs = getDataFieldCfgs(dataCfgOid);
        TableSchema tableSchema = new TableSchema();
        if (dataCfgTableInfo != null) {
            tableSchema.setTableName(dataCfgTableInfo.getTableName());
        }
        tableSchema.setDataOid(dataCfgOid);
        for (DataFieldCfg dataFieldCfg: dataFieldCfgs) {
            TableField tableField = new TableField();
            tableField.setId(dataFieldCfg.getFieldName());
            tableField.setType("string");
            tableField.setOriginalType(dataFieldCfg.getFieldType());
            tableSchema.addField(tableField);
        }
/*
        if (!tableSchema.getFields().containsById("_id")) {
            tableSchema.getFields().add(0, genId()); // 第一個位置
        }
*/
        return tableSchema;
    }

    private void createTable(final EntityManager entityManager, TableSchema tableSchema) {
        String tableName = tableSchema.getTableName();
        //產生hibernate的hbm設定檔
        Document hbm = DocumentHelper.createDocument();
        hbm.addDocType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 3.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
        Element mapping = hbm.addElement("hibernate-mapping");
        Element elementClass = mapping.addElement("class");
        elementClass.addAttribute("name", tableName);
        elementClass.addAttribute("table", "[" + tableName + "]");

        final Dialect dialect = getDialect(entityManager);

        Element id = elementClass.addElement("id")
                                 .addAttribute("name", "[id]")
                                 .addAttribute("type", "int")
                                 .addAttribute("column", "[_id]");
        id.addElement("generator").addAttribute("class", "native");
        
        //整理column跟type
        for (TableField field: tableSchema.getFields()) {
            String name = field.getId();
            String type = field.getType();
            if (StringUtils.equalsIgnoreCase("_id", name)) { // skip _id
                continue;
            }
            Element property = elementClass.addElement("property")
                                           .addAttribute("name", "[" + name + "]")
                                           .addAttribute("type", type);
            property.addElement("column")
                    .addAttribute("name", "[" + name + "]")
                    .addAttribute("sql-type", dialect.getTypeName(Types.NVARCHAR, 4000, 0 ,0));
        }
        
        log.debug(hbm.asXML());

        throw new RuntimeException("Create data table not implemented.");
/*
        // TODO create table
        final InputStream is = new ByteArrayInputStream(hbm.asXML().getBytes(StandardCharsets.UTF_8));
        final Session session = entityManager.unwrap(Session.class);
        session.doWork(new org.hibernate.jdbc.Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                MetadataSources metadataSource = new MetadataSources(
                        new StandardServiceRegistryBuilder()
                            .applySetting("hibernate.dialect", dialect.toString())
                            .build());
                metadataSource.addInputStream(is);
                MetadataImplementor metadata = (MetadataImplementor) metadataSource.buildMetadata();
                metadata.validate();
                SchemaExport export = new SchemaExport(metadata, connection);
                export.execute(true, true, false, true); // just create not drop
            }
        });
*/
/*
        final Session session = entityManager.unwrap(Session.class);
        final Session newSession = session.getSessionFactory().openSession();
        if (newSession.doReturningWork(new org.hibernate.jdbc.ReturningWork<Boolean>() {
                @Override
                public Boolean execute(Connection connection) throws SQLException {
                    MetadataSources metadataSource = new MetadataSources(
                            new StandardServiceRegistryBuilder()
                            .applySetting("hibernate.dialect", getDialect(entityManager).toString())
                            .build());
                    metadataSource.addInputStream(is);
                    MetadataImplementor metadata = (MetadataImplementor) metadataSource.buildMetadata();
                    metadata.validate();
                    SchemaExport export = new SchemaExport(metadata, connection);
                    export.execute(true, true, false, true); // just create not drop
                    return true;
                }})) {
            flush();
        }
*/
    }
    
    private void verifyTable(DataCfgTableInfo dataCfgTableInfo) {
        String filter = null;
        Pageable p = PageRequest.of(0, 1);
        queryData(dataCfgTableInfo, filter, p);
    }
    
    private Dialect getDialect(EntityManager entityManager) {
        Session session = (Session) entityManager.getDelegate();
        SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) session.getSessionFactory();
//        return sessionFactory.getDialect();
        return sessionFactory.getServiceRegistry().getService( JdbcServices.class ).getDialect();
    }

    /**
     * get TableSchema from DataCfgTableInfo, 含 _id 欄位
     * @param dataCfgTableInfo
     * @return TableSchema null if DataCfgTableInfo not exists or DataCfgTableInfo.tableSchema blank
     */
    private TableSchema getTableSchema(DataCfgTableInfo dataCfgTableInfo) {
        if (dataCfgTableInfo == null || StringUtils.isBlank(dataCfgTableInfo.getTableSchema()))
            return null;
        // table field from DataCfgTableInfoPo.tableSchema
        if (StringUtils.isNotBlank(dataCfgTableInfo.getTableSchema())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                TableSchema result = mapper.readValue(dataCfgTableInfo.getTableSchema(), TableSchema.class);
                result.setTableName(dataCfgTableInfo.getTableName());
                if (!result.getFields().containsById("_id")) {
                    result.getFields().add(0, genId()); // 第一個位置
                }
                return result;
            } catch (IOException e) {
                log.error(e);
            }
        }
        return null;
    }

    private TableField genId() {
        TableField id = new TableField();
        id.setId("_id");
        id.setType("string");
        return id;
    }

}
