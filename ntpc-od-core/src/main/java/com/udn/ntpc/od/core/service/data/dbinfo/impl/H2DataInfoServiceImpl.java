package com.udn.ntpc.od.core.service.data.dbinfo.impl;

import com.udn.ntpc.od.core.service.data.dbinfo.H2DataInfoService;
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
public class H2DataInfoServiceImpl extends AbstractDBDataInfoServiceImpl implements H2DataInfoService {

	/**
	 * fixed field name is reserved keyword SQL Server [%s], "%s" MySQL `%s`
	 * Oracle "%s" H2 databse "%s"
	 */
	private static final String[] DEFAULT_H2_FIELD_KEYWORD_WRAPPER = { "\"", "\"" };

	@Override
	protected String getUrl(String ip, String dbName) {
		return "jdbc:h2:file:../db/ntpc;MODE=MSSQLServer;DB_CLOSE_ON_EXIT=FALSE";
	}

	@Override
	protected String getDriver() {
		return "org.h2.Driver";
	}

	@Override
	protected String[] doGetFieldKeywordWrapper() {
		return DEFAULT_H2_FIELD_KEYWORD_WRAPPER;
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

        String sql = "select %s from %s where %s order by %s limit %s offset %s";
		String s = String.format(sql, selectedFields, getQuoteName(tableName), where, orderBy, maxResults, firstResult);
		
//		log.debug(s);
		return new DataPageImpl<>(jdbcTemplate.queryForList(s, parameters.getValues()), p, total, fields);
	}

}
