package com.udn.ntpc.od.core.service.data.dbinfo;

import com.udn.ntpc.od.common.converter.StringToSortObjectConverter;
import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.core.AbstractJUnit4TestCase;
import com.udn.ntpc.od.core.service.cfg.DataCfgConnectionParamService;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgDto;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.DBKey;
import com.udn.ntpc.od.model.common.FIELD_TYPES;
import com.udn.ntpc.od.model.domain.DataPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBConfigServiceTests extends AbstractJUnit4TestCase {

    private static final Log LOG = LogFactory.getLog(DBConfigServiceTests.class);
    
    @Autowired
    private DBConfigService service;

    @Autowired
    private DataCfgService dataCfgService;

    @Autowired
    private DataCfgConnectionParamService dataCfgConnectionParamService;

    @Autowired
    private StringToSortObjectConverter stringToSortObjectConverter;

    /**
     * 新北市寺廟資料, <br>
     * F0E1879F-3C5B-429F-A12B-9540ACAC26E8<br>
     */
    @Test
    public void testConnection() {
        String dataSetOid = "F0E1879F-3C5B-429F-A12B-9540ACAC26E8";
        DataCfgConnectionParams params = dataCfgConnectionParamService.findByDataSetOid(dataSetOid);
        LOG.info("測試前：" + params);
        Assert.assertTrue("連線測試失敗：" + params, service.testConnection(dataSetOid, params));
        LOG.info("測試後：" + params);
    }

    /**
     * 新北市寺廟資料, <br>
     * F0E1879F-3C5B-429F-A12B-9540ACAC26E8<br>
     */
    @Test
    public void tableData() {
        String dataSetOid = "F0E1879F-3C5B-429F-A12B-9540ACAC26E8";
        DataPage<Map<String, Object>> results = service.tableData(dataSetOid, "data_153", null);
        Assert.assertTrue("查無資料", results.hasContent());
        printAllDataResults(results.getContent());
    }

    /**
     * 新北市寺廟資料, <br>
     * F0E1879F-3C5B-429F-A12B-9540ACAC26E8<br>
     */
    @Test
    @Transactional
    public void save() throws ParseException {
        String dataSetOid = "F0E1879F-3C5B-429F-A12B-9540ACAC26E8";
        String strSort = stringToSortObjectConverter.convert("area:asc,name:desc").toString();
        String strStartTime = "2016-11-22 09:27:00";
        Date startTime = DateUtils.parseDate(strStartTime, ConnectionParam.DATE_TIME_PATTERN);
        DataFieldCfgsDto dataFieldCfgsDto = genDataFieldCfgsDto();
        CustomResult<String> result = service.save(dataSetOid, strSort, startTime, dataFieldCfgsDto);
        Assert.assertTrue("儲存其他資訊失敗", result.hasValue());
        DataCfgConnectionParams params = dataCfgConnectionParamService.findByDataSetOid(dataSetOid);
        Assert.assertTrue(String.format("sort 儲存失敗, %s - %s", strSort, params.getValue(DBKey.SORT)), strSort.equals(params.getValue(DBKey.SORT)));
        Assert.assertTrue(String.format("startTime 儲存失敗, %s - %s", strStartTime, params.getValue(DBKey.START_TIME)), strStartTime.equals(params.getValue(DBKey.START_TIME)));
        Set<DataFieldCfg> newFields = dataCfgService.getDataFieldConfigs(dataSetOid.toString());
//        Assert.assertTrue(String.format("DataFieldCfgsDto 儲存失敗, %s - %s", dataFieldCfgsDto.size(), newFields.size()), dataFieldCfgsDto.size() == (newFields.size() - 4)); // 扣除座標
        Assert.assertTrue(String.format("DataFieldCfgsDto 儲存失敗, %s - %s", dataFieldCfgsDto.size(), newFields.size()-4), dataFieldCfgsDto.size() == newFields.size()-4); // 扣除座標
    }
    
    private void printAllDataResults(List<Map<String, Object>> results) {
        LOG.info(String.format("共 %s 筆", results.size()));
        final int count = 5;
        for (Map<String, Object> data: results) {
            int icount = 0;
            String message = "";
            Set<String> fieldNames = data.keySet();
            for (String fieldName: fieldNames) {
                icount++;
                if (StringUtils.isNotBlank(message))
                    message += ", ";
                message += String.format("%s: %s", fieldName, data.get(fieldName));
                if (icount == count || icount >= fieldNames.size()) {
                    LOG.info(message);
                    break;
                }
            }
        }
    }

    private DataFieldCfgsDto genDataFieldCfgsDto() {
        DataFieldCfgsDto dataFieldCfgsDto = new DataFieldCfgsDto();
        
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_ID", FIELD_TYPES.COMMON.getValue(), "主鍵_流水號", BigInteger.valueOf(0)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_Area", FIELD_TYPES.COMMON.getValue(), "行政區'", BigInteger.valueOf(1)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_NAME", FIELD_TYPES.COMMON.getValue(), "寺廟名稱", BigInteger.valueOf(2)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_Village", FIELD_TYPES.COMMON.getValue(), "村里", BigInteger.valueOf(3)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_Address", FIELD_TYPES.ADDRESS.getValue(), "寺廟地址", BigInteger.valueOf(4)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_Phone", FIELD_TYPES.COMMON.getValue(), "電話", BigInteger.valueOf(5)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_God", FIELD_TYPES.COMMON.getValue(), "主祀神像", BigInteger.valueOf(6)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_Class", FIELD_TYPES.COMMON.getValue(), "教別", BigInteger.valueOf(7)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_BuildTime", FIELD_TYPES.COMMON.getValue(), "建立時間", BigInteger.valueOf(8)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_ReBuildTime", FIELD_TYPES.COMMON.getValue(), "重建/修建時間", BigInteger.valueOf(9)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("TEP_FestivalDate", FIELD_TYPES.COMMON.getValue(), "祭典日期", BigInteger.valueOf(10)));

        return dataFieldCfgsDto;
    }

}
