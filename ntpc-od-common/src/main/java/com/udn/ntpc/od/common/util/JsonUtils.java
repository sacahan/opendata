package com.udn.ntpc.od.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static final Log LOG = LogFactory.getLog(JsonUtils.class);
    
    public static String readCsvToJson(String csv) {
		try {
		    // CRLF->LF (JSON套件限制)
		    csv = csv.replaceAll("\\r\\n", "\\\n");
			JSONArray array = CDL.toJSONArray(csv);
			return array.toString();
		} catch (JSONException e) {
		    LOG.error(e.getLocalizedMessage(), e);
		}
		return null;
    }

    /**
     * 將XML結構完整轉成JSON格式
     * @param xml
     * @return
     */
    public static String readXmlToJson(String xml) {
        try {
            // json格式
            JSONObject jObject = XML.toJSONObject(xml);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // map物件
            Object json = mapper.readValue(jObject.toString(), Object.class);
            // format過的json
			return mapper.writeValueAsString(json);
        } catch (JSONException | IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
	
	/**
	 * 
	 * @param xml
	 * @return
	 */
/* 會自動指定 root，但不穩定
	public static String readXmlToJsonList(String xml){
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xml);
		return json.toString();
	}
*/
	/**
	 * 將 xml 轉成 json 並取得 rootPath 下所有內容 
	 * @param xml
	 * @param rootPath 假定 rootPath 最後取得的是 Array
	 * @return
	 */
	public static String readXmlToJsonList(String xml, String rootPath){
		String json = readXmlToJson(xml);
		return readJsonToJsonList(json, rootPath);
	}
    
	/**
	 * 取得 rootPath 下所有內容 root: "" or "/"
	 * @param json
	 * @param rootPath 假定 rootPath 最後取得的是 Array
	 * @return
	 */
	public static String readJsonToJsonList(String json, String rootPath) {
        // 無指定路徑
        if (StringUtils.isBlank(rootPath)) { // rootPath = "";
            JsonElement jsonElement = (new JsonParser()).parse(json);
            if (jsonElement.isJsonArray()) {
                return jsonElement.toString();
            }
            return transJsonObjectToArray(jsonElement).toString();
        }

	    // 路徑除去"/"開頭，遞迴呼叫本身方法
	    if (rootPath.trim().startsWith("/")) {
	        rootPath = rootPath.trim().substring(1);
	        return readJsonToJsonList(json, rootPath);
	    }
	    
	    JsonElement jsonElement = (new JsonParser()).parse(json);
	    JsonObject object = jsonElement.isJsonArray()? 
	                            getJsonArrayFirstObject(jsonElement.getAsJsonArray()):
                                jsonElement.getAsJsonObject();
	    if (object == null || object.isJsonNull()) {
	        return "[]";
	    }
	    // 依路徑找目標資料
	    String[] rootList = rootPath.split("/");
	    for (String path : rootList) {
	        // 取路徑下的物件
	        JsonElement obj = object.get(path);
	        if (obj.isJsonObject()) {
                if (rootPath.endsWith(path)) {
                    return transJsonObjectToArray(obj).toString();
                }
	            // 指定到下一層物件
	            object = obj.getAsJsonObject();
	        } else if (obj.isJsonArray()) {
	            // 目標底層
                if (rootPath.endsWith(path)) {
                    return obj.toString();
                }
	            // 遇到JsonArray，只取Array中第一個物件
	            object = getJsonArrayFirstObject(obj.getAsJsonArray());
	        }
	    }
	    
	    // 目標path只有單一項目
	    // 透過 rootPath 找不到 Array
	    return transJsonObjectToArray(object).toString();
	}
	
    private static JsonArray transJsonObjectToArray(JsonElement jsonObject) {
        JsonArray jsonArray = new JsonArray();
        if (jsonObject != null && !jsonObject.isJsonNull()) {
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private static JsonObject getJsonArrayFirstObject(JsonArray jsonArray) {
        if (jsonArray != null && !jsonArray.isJsonNull() && jsonArray.size() > 0) {
            return jsonArray.get(0).getAsJsonObject();
        }
        return null;
    }

/*	
    public static String readJsonToJsonList(String json, String rootPath) {
    	// 無指定路徑
        if (StringUtils.isBlank(rootPath)) {
//            rootPath = "";
        	if (isJsonArray(json)) {
            	return json;
            }
        	return transJsonObjectToArray(new JSONObject(json)).toString();
        }
        
        // 路徑除去"/"開頭，遞迴呼叫本身方法
        if (rootPath.trim().startsWith("/")) {
            rootPath = rootPath.trim().substring(1);
            return readJsonToJsonList(json, rootPath);
        }
        
        JSONObject object = new JSONObject();
        // json 本身是一個 Array，只取Array中第一個物件
        if (isJsonArray(json)) {
        	object = getJsonArrayFirstObject(new JSONArray(json));
        }else{
        	object = new JSONObject(json);
        }
        
        // 依路徑找目標資料
        String[] rootList = rootPath.split("/");
        for (String path : rootList) {
        	// 取路徑下的物件
            Object obj = object.get(path);
            if (obj instanceof JSONObject) {
            	if(rootPath.endsWith(path)){
            		return transJsonObjectToArray((JSONObject) obj).toString();
            	}
            	// 指定到下一層物件
                object = (JSONObject) obj;
            } else if (obj instanceof JSONArray) {
                // 目標底層
                if(rootPath.endsWith(path)){
                	return obj.toString();
                }
                // 遇到JsonArray，只取Array中第一個物件
                object = getJsonArrayFirstObject((JSONArray) obj);
            }
        }
        
        // 目標path只有單一項目
        // 透過 rootPath 找不到 Array
       	return transJsonObjectToArray(object).toString();
    }
    
	private static JSONArray transJsonObjectToArray(JSONObject jsonObject){
	    JSONArray jsonArray = new JSONArray();
	    if(jsonObject!=null){
	        jsonArray.put(jsonObject);
	    }
	    return jsonArray;
	}
	
	private static JSONObject getJsonArrayFirstObject(JSONArray jsonArray){
	    return jsonArray.getJSONObject(0);
	}
	
	private static Boolean isJsonArray(String json) {
	    if (StringUtils.isBlank(json)) {
	        return false;
	    }
	    return json.trim().startsWith("[");
	}
*/    
    
}
