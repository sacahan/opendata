package com.udn.ntpc.od.model.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 取得 JSON 所有欄位資訊<br>
/
/EssentialInfo
/EssentialInfo/Location
/BusInfo
{<br>
　"EssentialInfo": {<br>
　　"Location": {<br>
　　　　"name": "台北市",<br>
　　　　"CenterName": "台北市公車動態資訊中心"<br>
　　},<br>
　　"UpdateTime": "2017/04/26 15:01:49",<br>
　　"CoordinateSystem": "WGS84"<br>
　},<br>
　"BusInfo": [<br>
　{<br>
　　"Id": 16586,<br>
　　"providerId": 16176,<br>
　　"providerName": "臺北客運",<br>
　　"nameZh": "藍46",<br>
　　"nameEn": "BL46"<br>
　},<br>
　{<br>
　　"Id": 16515,<br>
　　"providerId": 16179,<br>
　　"providerName": "三重客運",<br>
　　"nameZh": "858",<br>
　　"nameEn": "858"<br>
　}<br>
}<br>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonFieldInfoParser {

/*
    private String json;
	private Map<String, List<TableField>> rootFieldList = new TreeMap<String, List<TableField>>();
	
	public JsonFieldInfoParser(String json) throws JSONException {
	    this.json = json;
		setJsonPaths(this.json, "");
	}
	
	public Map<String, List<TableField>> getRootFieldList() {
	    return this.rootFieldList;
	}
*/
	public static Map<String, TableFields> getRootFieldList(String json) {
        return getRootFieldList(json, "");
	}

	/**
	 * gson version
	 * @param json
	 * @param jsonPath
	 * @return
	 */
	private static Map<String, TableFields> getRootFieldList(String json, String jsonPath) {
	    Map<String, TableFields> rootFields = new TreeMap<>();
	    JsonElement jsonElement = (new JsonParser()).parse(json);
	    if (StringUtils.isNotBlank(json) && !jsonElement.isJsonNull()) {
	        if (jsonElement.isJsonArray()) {
                readArray(rootFields, jsonElement.getAsJsonArray(), jsonPath);
	        } else {
	            readObject(rootFields, jsonElement.getAsJsonObject(), jsonPath);
	        }
	    }
	    return rootFields;
	}
	
	/**
	 * gson version
	 * @param rootFields
	 * @param object
	 * @param jsonPath
	 */
    private static void readObject(Map<String, TableFields> rootFields, JsonObject object, String jsonPath) {
        Iterator<String> keys = object.keySet().iterator();
        String parentPath = jsonPath;
        
//      List<TableField> fieldList = new ArrayList<TableField>();
        TableFields fieldList = new TableFields();
        while (keys.hasNext()) {
            String key = keys.next();
            JsonElement value = object.get(key);
            jsonPath = parentPath + "/" + key;
            
            TableField field = new TableField(key, jsonPath, value.isJsonPrimitive()? value.getAsString(): value.toString());
            fieldList.add(field);
            
            if (value.isJsonArray()) {
                readArray(rootFields, value.getAsJsonArray(), jsonPath);
            } else if (value.isJsonObject()) {
                readObject(rootFields, value.getAsJsonObject(), jsonPath);
            } else {
//              TableField fieldInfo = new TableField(key, jsonPath, value.toString());
//              this.fieldInfoList.add(fieldInfo);
            }
        }
//      this.rootFieldList.put(parentPath.isEmpty()?"/":parentPath, fieldList);
        rootFields.put(parentPath.isEmpty()?"/":parentPath, fieldList);
    }

    private static void readArray(Map<String, TableFields> rootFields, JsonArray array, String jsonPath) {
        // 欄位資訊只需從陣列中的第一組資料取得
        if (array.size() > 0) {
            JsonElement value = array.get(0);
            if (value.isJsonArray()) {
                readArray(rootFields, value.getAsJsonArray(), jsonPath);
            } else if (value.isJsonObject()) {
                readObject(rootFields, value.getAsJsonObject(), jsonPath);
            }
        }
    }
/* org.json version	
    private static Map<String, TableFields> getRootFieldList(String json, String jsonPath) throws JSONException {
        Map<String, TableFields> rootFields = new TreeMap<String, TableFields>();
        if (StringUtils.isNotBlank(json) && json != JSONObject.NULL) {
            if (isJsonArray(json)) {
                JSONArray array = new JSONArray(json);
                readArray(rootFields, array, jsonPath);
            } else {
                JSONObject object = new JSONObject(json);
                readObject(rootFields, object, jsonPath);
            }
        }
        return rootFields;
    }

	@SuppressWarnings("unchecked")
	private static void readObject(Map<String, TableFields> rootFields, JSONObject object, String jsonPath) throws JSONException {
		Iterator<String> keysItr = object.keys();
		String parentPath = jsonPath;
		
//		List<TableField> fieldList = new ArrayList<TableField>();
		TableFields fieldList = new TableFields();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			jsonPath = parentPath + "/" + key;
			
			TableField field = new TableField(key, jsonPath, value.toString());
			fieldList.add(field);
			
			if (value instanceof JSONArray) {
				readArray(rootFields, (JSONArray) value, jsonPath);
			} else if (value instanceof JSONObject) {
				readObject(rootFields, (JSONObject) value, jsonPath);
			} else {
//				TableField fieldInfo = new TableField(key, jsonPath, value.toString());
//				this.fieldInfoList.add(fieldInfo);
			}
		}
//		this.rootFieldList.put(parentPath.isEmpty()?"/":parentPath, fieldList);
		rootFields.put(parentPath.isEmpty()?"/":parentPath, fieldList);
	}
	
	private static void readArray(Map<String, TableFields> rootFields, JSONArray array, String jsonPath) throws JSONException {
		// 欄位資訊只需從陣列中的第一組資料取得
	    if (array.length() > 0) {
    		Object value = array.get(0);
    		if (value instanceof JSONArray) {
    			readArray(rootFields, (JSONArray) value, jsonPath);
    		} else if (value instanceof JSONObject) {
    			readObject(rootFields, (JSONObject) value, jsonPath);
    		}
		}
	}

	private static Boolean isJsonArray(String json) {
	    if (StringUtils.isBlank(json)) {
	        return false;
	    }
		return json.trim().startsWith("[");
	}
*/
}
