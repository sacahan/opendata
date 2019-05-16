package com.udn.ntpc.od.core.service.data.dbinfo;

import com.udn.ntpc.od.common.variables.Profiles;
import com.udn.ntpc.od.core.AbstractJUnit4TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import com.udn.ntpc.od.model.common.ConnectionParam.DBKey_DB_TYPE;

import javax.annotation.Resource;
import java.util.Map;

public class DataSourceUtilTests extends AbstractJUnit4TestCase {
    
    @Resource(name = "dbDataInfoServices")
    private Map<String, DBDataInfoService> dbDataInfoServices;

    @Autowired
    private DataSourceUtil dataSourceUtil;

    @Value("${db.driverClassName}")
    private String driverClassName = "org.h2.Driver";
    @Value("${db.url}")
    private String url = "jdbc:h2:file:../db/ntpc;MODE=MSSQLServer;DB_CLOSE_ON_EXIT=FALSE";
    @Value("${db.username}")
    private String user = "sa";
    @Value("${db.password}")
    private String password = "";
    
    @Test
    public void testConnectionByJdbcTemplate() {
        JdbcTemplate jdbcTemplate = dataSourceUtil.getJdbcTemplate(driverClassName, url, user, password);
        Assert.assertTrue("連線測試失敗, " + url, DataSourceUtil.validationQuery(jdbcTemplate, url));
    }

    /**
     * H2 不使用 ip, dbName
     */
    @Profile(Profiles.DEV)
    @Test
    public void testConnectionByDBDataInfoDev() {
        DBDataInfoService dbDataInfoService = dbDataInfoServices.get(DBKey_DB_TYPE.H2.name());
        Assert.assertTrue("連線測試失敗, " + url, dbDataInfoService.checkConnection("localhost", "ntpc", user, password));
    }

}
