package com.udn.ntpc.od.core.service.data.dbinfo;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 以 database url 當成 key 記錄所有 DataSource 及 JdbcTemplate
 * @author USER
 *
 */
@Service
@Slf4j
public class DataSourceUtil {
    private static final Map<String, DataSource> dataSources = new LinkedHashMap<>();
    private static final Map<String, JdbcTemplate> jdbcTemplates = new LinkedHashMap<>();
    private static final String KEY_PATTERN = "%s-%s-%s"; // url-user
    private static final String TEST_KEY_PATTERN = "t_%s-%s"; // t_url-user
    
    @Value("${batch.dbcp2.minIdle:1}")
    private int minIdle;

    @Value("${batch.dbcp2.maxTotal:50}")
    private int maxTotal;

/*
    @PostConstruct
    private void postConstruct() {
    }
*/

    @PreDestroy
    private void preDestroy() {
        log.info(String.format("------ %s JdbcTemplates will be clear", jdbcTemplates.size()));
        log.info(String.format("------ %s DataSources will be clear", dataSources.size()));
        jdbcTemplates.clear();
        dataSources.clear();
    }
    
    public DataSource getDataSource(String driverClassName, String url, String user, String password) {
        final String key = genKey(url, user, password);
        return getDataSource(key, driverClassName, url, user, password);
    }
    
    public JdbcTemplate getJdbcTemplate(String driverClassName, String url, String user, String password) {
        final String key = genKey(url, user, password);
        return getJdbcTemplate(key, driverClassName, url, user, password);
    }

    public JdbcTemplate getJdbcTemplate(DataSource dataSource, String url, String user, String password) {
        final String key = genKey(url, user, password);
        return getJdbcTemplate(dataSource, key);
    }
    
    public JdbcTemplate getTestJdbcTemplate(String driverClassName, String url, String user, String password) {
        final String key = genTestKey(url, user);
        return getJdbcTemplate(key, driverClassName, url, user, password);
    }

    public JdbcTemplate getTestJdbcTemplate(DataSource dataSource, String url, String user) {
        final String key = genTestKey(url, user);
        return getJdbcTemplate(dataSource, key);
    }
    
    private DataSource getDataSource(String key, String driverClassName, String url, String user, String password) {
        log.debug("getDataSource: " + key);
/*
        DataSource result = dataSources.get(key);
        if (result == null) {
            result = dataSource(driverClassName, url, user, password, key);
            dataSources.put(key, result);
        }
*/
//        DataSource result = dataSources.putIfAbsent(key, dataSource(driverClassName, url, user, password, key)); // always return null
        DataSource result = dataSources.computeIfAbsent(key, k -> dataSource(driverClassName, url, user, password, key));
        log.info(String.format("------ %s DataSources in pool", dataSources.size()));
        return result;
    }
    
    private JdbcTemplate getJdbcTemplate(String key, String driverClassName, String url, String user, String password) {
        DataSource dataSource = getDataSource(key, driverClassName, url, user, password);
        return getJdbcTemplate(dataSource, key);
    }
    
    private JdbcTemplate getJdbcTemplate(DataSource dataSource, String key) {
        log.debug("getJdbcTemplate: " + key);
/*
        JdbcTemplate result = jdbcTemplates.get(key);
        if (result == null) {
            result = new JdbcTemplate(dataSource);
            jdbcTemplates.put(key, result);
        }
*/
//        JdbcTemplate result = jdbcTemplates.putIfAbsent(key, new JdbcTemplate(dataSource)); // always return null
        JdbcTemplate result = jdbcTemplates.computeIfAbsent(key, k -> new JdbcTemplate(dataSource));
        log.info(String.format("------ %s JdbcTemplates in pool", jdbcTemplates.size()));
        return result;
    }

    private HikariDataSource dataSource(String driverClassName, String url, String user, String password, String key) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(minIdle);
        dataSource.setMaximumPoolSize(maxTotal);
        dataSource.setPoolName(key);
        dataSource.setMaxLifetime(1800000);
        dataSource.setConnectionTimeout(30000);
        dataSource.setConnectionTestQuery(getValidationQuery(url));
        return dataSource;
    }

    public static void removeDataSource(DataSource dataSource) {
        if (dataSources.containsValue(dataSource)) {
            dataSources.values().remove(dataSource);
            ((HikariDataSource) dataSource).close();
            log.info(String.format("------ %s DataSources in pool", dataSources.size()));
        }
    }
    
    public static void removeJdbcTemplate(JdbcTemplate jdbcTemplate) {
        if (jdbcTemplates.containsValue(jdbcTemplate)) {
//            removeDataSource(jdbcTemplate.getDataSource());
            jdbcTemplates.values().remove(jdbcTemplate);
            log.info(String.format("------ %s JdbcTemplates in pool", jdbcTemplates.size()));
        }
    }

    public static boolean validationQuery(JdbcTemplate jdbcTemplate, String url) {
        jdbcTemplate.execute(getValidationQuery(url));
        return true;
    }

    private static String genKey(String url, String user, String password) {
        return String.format(KEY_PATTERN, url, user, DigestUtils.md5Hex(password + user));
    }

    private static String genTestKey(String url, String user) {
        return String.format(TEST_KEY_PATTERN, url, user) + "-" + UUID.randomUUID().toString();
    }

    private static String getValidationQuery(String url) {
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(url);
        return databaseDriver.getValidationQuery();
    }
    
}
