package com.udn.ntpc.od.core.service.data.dbinfo.impl;

import com.udn.ntpc.od.core.service.data.dbinfo.MySQLDataInfoService;
import com.udn.ntpc.od.common.variables.DEFAULT_SETTINGS;
import com.udn.ntpc.od.model.domain.DataPage;
import com.udn.ntpc.od.model.domain.DataPageImpl;
import com.udn.ntpc.od.model.domain.Parameters;
import com.udn.ntpc.od.model.domain.TableFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class MySQLDataInfoServiceImpl extends AbstractDBDataInfoServiceImpl implements MySQLDataInfoService {

	private static final String[] DEFAULT_MYSQL_FIELD_KEYWORD_WRAPPER = { "`", "`" };

	@Override
	protected String getUrl(String ip, String dbName) {
        return "jdbc:mysql://" + ip + "/" + dbName;
	}

	@Override
	protected String getDriver() {
        return "com.mysql.jdbc.Driver";
	}

	@Override
	protected String[] doGetFieldKeywordWrapper() {
		return DEFAULT_MYSQL_FIELD_KEYWORD_WRAPPER;
	}

    @Override
    protected DataPage<Map<String, Object>> doQueryData(JdbcTemplate jdbcTemplate, TableFields fields, String selectedFields,
                                                        String tableName, String where, String orderBy, Parameters parameters, Pageable p) {
        long total = dataCount(jdbcTemplate, tableName, where, parameters);
        if (total == 0) {
            return new DataPageImpl<>(new ArrayList<Map<String, Object>>(), p, total);
        }

        if (p == null) {
            p = DEFAULT_SETTINGS.DEFAULT_DATA_PAGING;
        }

        long firstResult = p.getOffset();
        int maxResults = p.getPageSize();
        String limit = String.format("%s,%s", firstResult, maxResults);

        StringBuilder sql = new StringBuilder();
        sql.append("select %s from %s ");
        sql.append(" where " + where);
        sql.append(" order by %s");
        sql.append(" limit %s ");

        String s = String.format(sql.toString(), selectedFields, getQuoteName(tableName), orderBy, limit);

//        log.debug(s);
        return new DataPageImpl<>(jdbcTemplate.queryForList(s, parameters.getValues()), p, total, fields);

    }

}
