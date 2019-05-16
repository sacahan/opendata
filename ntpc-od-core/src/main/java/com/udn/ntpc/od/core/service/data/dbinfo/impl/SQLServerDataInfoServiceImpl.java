package com.udn.ntpc.od.core.service.data.dbinfo.impl;

import com.udn.ntpc.od.core.service.data.dbinfo.SQLServerDataInfoService;
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
public class SQLServerDataInfoServiceImpl extends AbstractDBDataInfoServiceImpl implements SQLServerDataInfoService {

	/**
	 * fixed field name is reserved keyword SQL Server [%s], "%s" MySQL `%s`
	 * Oracle "%s" H2 databse "%s"
	 */
	private static final String[] DEFAULT_SQL_SERVER_FIELD_KEYWORD_WRAPPER = { "[", "]" };

	@Override
	protected String getUrl(String ip, String dbName) {
		return "jdbc:sqlserver://" + ip + ";DatabaseName=" + dbName;
	}

	@Override
	protected String getDriver() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	protected String[] doGetFieldKeywordWrapper() {
		return DEFAULT_SQL_SERVER_FIELD_KEYWORD_WRAPPER;
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

		int firstResult = p.getPageNumber() * p.getPageSize() + 1;
		int maxResults = (p.getPageNumber() + 1) * p.getPageSize();

		StringBuilder sql = new StringBuilder();
		sql.append("select %s from ( ");
		sql.append("    select row_number() over (order by %s) as _rownum, ");
		sql.append("           * ");
		sql.append("    from %s ");
		sql.append("    where " + where);
		sql.append(") as _dataTable ");
		sql.append("where _rowNum between %s and %s ");

		String s = String.format(sql.toString(), selectedFields, orderBy, getQuoteName(tableName), firstResult, maxResults);

//		log.debug(s);
		return new DataPageImpl<>(jdbcTemplate.queryForList(s, parameters.getValues()), p, total, fields);
	}

}
