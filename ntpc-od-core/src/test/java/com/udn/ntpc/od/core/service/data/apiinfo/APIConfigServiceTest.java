package com.udn.ntpc.od.core.service.data.apiinfo;

import com.udn.ntpc.od.common.variables.CustomResult;
import com.udn.ntpc.od.core.AbstractJUnit4TestCase;
import com.udn.ntpc.od.core.service.cfg.DataCfgConnectionParamService;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgDto;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey;
import com.udn.ntpc.od.model.common.FIELD_TYPES;
import com.udn.ntpc.od.model.domain.TableField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class APIConfigServiceTest extends AbstractJUnit4TestCase {

    @Autowired
    private APIConfigService apiConfigService;
    
    @Autowired
    private DataCfgService dataCfgService;
    
    @Autowired
    private DataCfgConnectionParamService dataCfgConnectionParamService;

    /**
     * 新北市申辦e服務, <br>
     * 394D1632-69FC-4792-A74D-88F5B1B46036<br>
     * https://service.ntpc.gov.tw/counterKM/OpenCaseData<br>
     * JSON
     */
    @Test
    public void testConnectionTest() {
	    final int expectedParamCount = 8;
	    final String expectedSort = "ITEM_ID: DESC";
	    final String expectedRoot = "/";
	    final int expectedFieldCount = 19;

    	final String dataSetOid = "394D1632-69FC-4792-A74D-88F5B1B46036";
    	DataCfgAPIConnectionParams params = dataCfgConnectionParamService.findAPIByDataSetOid(dataSetOid.toString());
    	Assert.assertEquals(expectedParamCount, params.size());
        Assert.assertEquals(expectedSort, params.getValue(APIKey.SORT).toString());
        Assert.assertEquals(expectedRoot, params.getValue(APIKey.ROOT_PATH).toString());

    	Map<String, Object> results = apiConfigService.testConnection(params);
    	Assert.assertTrue(MapUtils.isNotEmpty(results));

        log.debug(results.entrySet().stream().findFirst().get().getValue().toString());

    	Map<String, List<TableField>> rootFields = (Map<String, List<TableField>>) results.get(ConnectionParam.FIELDS);
    	List<TableField> fields = rootFields.get(expectedRoot);
    	
        Assert.assertNotNull(results.get(ConnectionParam.RAWDATA));
        Assert.assertEquals(expectedFieldCount, fields.size());
    }

    /**
     * 新北市申辦e服務, <br>
     * 394D1632-69FC-4792-A74D-88F5B1B46036<br>
     * https://service.ntpc.gov.tw/counterKM/OpenCaseData<br>
     * @throws ParseException
     */
    @Test
    @Transactional
    public void saveDataFieldCfgsTest() throws ParseException {
        String dataSetOid = "394D1632-69FC-4792-A74D-88F5B1B46036";
        String strSort = "ITEM_ID: DESC";
        String strStartTime = "2016-11-22 09:27:00";
        Date startTime = DateUtils.parseDate(strStartTime, ConnectionParam.DATE_TIME_PATTERN);
        
        DataFieldCfgsDto dataFieldCfgsDto = genDataFieldCfgsDto();
        CustomResult<String> result = apiConfigService.save(dataSetOid, strSort, startTime, dataFieldCfgsDto);
        Assert.assertTrue("儲存欄位資訊失敗", result.hasValue());

        Set<DataFieldCfg> newFields = dataCfgService.getDataFieldConfigs(dataSetOid);
        Assert.assertTrue(String.format("DataFieldCfgsDto 儲存失敗, %s - %s", dataFieldCfgsDto.size(), newFields.size()), dataFieldCfgsDto.size() == newFields.size());
    }

    /**
     * 新北市申辦e服務, <br>
     * 394D1632-69FC-4792-A74D-88F5B1B46036<br>
     * https://service.ntpc.gov.tw/counterKM/OpenCaseData<br>
     * @throws ParseException
     */
    @Test
    @Transactional
    public void saveDataCfgConnectionParamsTest() throws ParseException {
        /** 測試儲存值*/
        String dataSetOid = "394D1632-69FC-4792-A74D-88F5B1B46036";
        String strSort = "ITEM_ID: DESC";
        String strStartTime = "2016-11-22 09:27:00";
        Date startTime = DateUtils.parseDate(strStartTime, ConnectionParam.DATE_TIME_PATTERN);

        DataCfgAPIConnectionParams params = dataCfgConnectionParamService.findAPIByDataSetOid(dataSetOid);
        String oriSort = params.getValue(APIKey.SORT);// 比對用值
        params.setValue(APIKey.SORT, strSort);
        params.setValue(APIKey.START_TIME, DateFormatUtils.format(startTime, ConnectionParam.DATE_TIME_PATTERN));

        dataCfgService.saveDataCfgConnectionParams(dataSetOid, params);

        params = dataCfgConnectionParamService.findAPIByDataSetOid(dataSetOid);
        Assert.assertTrue(String.format("sort 儲存失敗, %s - %s", strSort, params.getValue(APIKey.SORT)), strSort.equals(params.getValue(APIKey.SORT)));
        Assert.assertTrue(String.format("startTime 儲存失敗, %s - %s", strStartTime, params.getValue(APIKey.START_TIME)), strStartTime.equals(params.getValue(APIKey.START_TIME)));
    }

    private DataFieldCfgsDto genDataFieldCfgsDto() {
        DataFieldCfgsDto dataFieldCfgsDto = new DataFieldCfgsDto();
        
        dataFieldCfgsDto.add(new DataFieldCfgDto("ITEM_ID", FIELD_TYPES.COMMON.getValue(), "案件編號", "/ITEM_ID", BigInteger.valueOf(0)));
        dataFieldCfgsDto.add(new DataFieldCfgDto("ITEM_NAME", FIELD_TYPES.COMMON.getValue(), "申辦名稱'", "/ITEM_NAME", BigInteger.valueOf(1)));
        
        return dataFieldCfgsDto;
    }

}
