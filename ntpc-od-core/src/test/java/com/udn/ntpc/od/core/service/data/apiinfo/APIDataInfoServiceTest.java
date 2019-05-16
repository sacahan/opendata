package com.udn.ntpc.od.core.service.data.apiinfo;

import com.udn.ntpc.od.core.AbstractJUnit4TestCase;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_METHOD;
import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_TYPE;
import com.udn.ntpc.od.model.domain.TableField;
import com.udn.ntpc.od.model.domain.TableFields;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class APIDataInfoServiceTest extends AbstractJUnit4TestCase {

	@Resource(name = "apiDataInfoServices")
	private Map<String, APIDataInfoService> apiDataInfoServices;

	@Test
	public void apiCsvDataTest() {
	    // test.csv
		String apiUrl = "https://drive.google.com/uc?export=download&id=1oArSiTEs9vALHDLA5obGgz1aof38_Iob";
		APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.CSV.name());
		APIKey_API_METHOD apiMethod = APIKey_API_METHOD.GET;
		String data = apiDataInfoService.apiData(apiUrl, apiMethod, (Object) null);		
        Assert.assertNotNull(String.format("API取得失敗, %s", apiUrl), data);
	}
	
	@Test
	public void getCsvFieldConfigsTest() {
        // test.csv
        String apiUrl = "https://drive.google.com/uc?export=download&id=1oArSiTEs9vALHDLA5obGgz1aof38_Iob";
	    APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.CSV.name());
	    APIKey_API_METHOD apiMethod = APIKey_API_METHOD.GET;
	    String data = apiDataInfoService.apiData(apiUrl, apiMethod, (Object) null);
	    Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
	    TableFields fieldsInfo = rootFieldsInfo.entrySet().iterator().next().getValue();
	    List<String> actualList = getActualList(fieldsInfo);
	    List<String> expectList = getExpectList();
	    
	    Assert.assertEquals(4, fieldsInfo.size());
	    Assert.assertEquals(expectList, actualList);
	}
/*	
	@Test
	public void apiJsonDataFromGzTest() {
	    String apiUrl = "http://10.201.80.115/utils/ungz/json?url=http://10.201.80.115/test/ntpc/bus/GetProvider.gz&encoding=UTF-8";
	    APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.JSON.name());
	    APIKey_API_METHOD apiMethod = APIKey_API_METHOD.GET;
	    String data = apiDataInfoService.apiData(apiUrl, apiMethod, (Object) null);		
	    Assert.assertTrue(String.format("API取得失敗, %s", apiUrl), data!=null);
	}
*/
    @Test
    public void getJsonBusRouteFieldConfigsTest() throws IOException, URISyntaxException {
        // first 100 bytes
        final String expected = "{\"EssentialInfo\":{\"Location\":{\"name\":\"台北市\",\"CenterName\":\"台北市公車動態資訊中心\"},\"UpdateTime\":\"2017/04/26 15:0";
        // GetRoute.gz
        String apiUrl = "https://drive.google.com/uc?export=download&id=1mTUjPpgxwd7du9Qe2VKX5gcGH4OHdBDw";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.JSON.name());
        String data = apiDataInfoService.ungzApiData(apiUrl);
        String actual = (data.length() >= 100)? data.substring(0, 100): "";
        Assert.assertEquals(expected, actual);

        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/BusInfo");
        Assert.assertEquals(35, fieldsInfo.size());
    }

    @Test
    public void getJsonUtf8BomFieldConfigsTest() {
        // apiData-BOM.json
        String apiUrl = "https://drive.google.com/uc?export=download&id=14xXobLvnFsRHkMxjsBO1cF43XG4Ko5do";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.JSON.name());
        String data = apiDataInfoService.apiData(apiUrl, APIKey_API_METHOD.GET, (Object) null);
        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/");
        
        Assert.assertEquals(22, fieldsInfo.size());
    }
    
    @Test
    public void getJsonBusStopFieldConfigsTest() throws IOException, URISyntaxException {
        // first 100 bytes
        final String expected = "{\"EssentialInfo\":{\"Location\":{\"name\":\"台北市\",\"CenterName\":\"台北市公車動態資訊中心\"},\"UpdateTime\":\"2017/04/26 15:0";
        // GetStop.gz
        String apiUrl = "https://drive.google.com/uc?export=download&id=19zf7jSZ2KZmgVUi4x7sx4FMXeoqimy96";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.JSON.name());
        String data = apiDataInfoService.ungzApiData(apiUrl);
        String actual = (data.length() >= 100)? data.substring(0, 100): "";
        Assert.assertEquals(expected, actual);

        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/BusInfo");
        Assert.assertEquals(14, fieldsInfo.size());
    }

    @Test
    public void getJsonBusEstimateTimeFieldConfigsTest() throws IOException, URISyntaxException {
        // first 100 bytes
        final String expected = "{\"EssentialInfo\":{\"Location\":{\"name\":\"台北市\",\"CenterName\":\"台北市公車動態資訊中心\"},\"UpdateTime\":\"2017/04/26 15:2";
        // GetEstimateTime.gz
        String apiUrl = "https://drive.google.com/uc?export=download&id=1cOZ5Ri9OFKfZCBZOhWeQslyKDSJrBKBy";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.JSON.name());
        String data = apiDataInfoService.ungzApiData(apiUrl);
        String actual = (data.length() >= 100)? data.substring(0, 100): "";
        Assert.assertEquals(expected, actual);

        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/BusInfo");
        Assert.assertEquals(4, fieldsInfo.size());
    }
    
    @Test
    public void getXmlEStoreKoreaFieldConfigsTest() {
        // estore_kr.xml
        String apiUrl = "https://drive.google.com/uc?export=download&id=1kTI9JR2bICO1zqpcWaB_wtLN1_u1xieo";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.XML.name());
        String data = apiDataInfoService.apiData(apiUrl, APIKey_API_METHOD.GET, (Object) null);
        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/XML_Head/Infos/Info");
        
        Assert.assertEquals(20, fieldsInfo.size());

        final String FIELD_DESCRIPTION = "Description";
        
        final int fieldIndex = fieldsInfo.indexOfById(FIELD_DESCRIPTION);
        
        Assert.assertTrue("Description field not found", fieldIndex > -1);

        final String expectDescription = "필매품추천:진산시아 소뿔빵";
        TableField description = fieldsInfo.get(fieldIndex);
        
        Assert.assertEquals("取得的資料內容不符", expectDescription, description.getPreviewValue());
    }
    
    @Test
    public void getXmlSchoolFieldConfigsTest() {
        // schools.xml
        String apiUrl = "https://drive.google.com/uc?export=download&id=1S08GoBQCnn-8O_ACwngRyJn5VION1KiB";
        APIDataInfoService apiDataInfoService = apiDataInfoServices.get(APIKey_API_TYPE.XML.name());
        String data = apiDataInfoService.apiData(apiUrl, APIKey_API_METHOD.GET, (Object) null);
        Map<String, TableFields> rootFieldsInfo = apiDataInfoService.getFieldConfigs(data);
        TableFields fieldsInfo = rootFieldsInfo.get("/ArrayOfSchool/School");
        
        Assert.assertEquals(18, fieldsInfo.size());
    }
    
	private List<String> getActualList(TableFields fieldsInfo) {
		List<String> actualList = new ArrayList<String>();
		for (TableField field : fieldsInfo) {
			actualList.add(field.getId());
		}
		Collections.sort(actualList);
		return actualList;
	}

	private List<String> getExpectList() {
		List<String> expectList = new ArrayList<String>();
		expectList.add("RouteID");
		expectList.add("StationId");
		expectList.add("EstimateTime");
		expectList.add("Memo");
		Collections.sort(expectList);
		return expectList;
	}
}
