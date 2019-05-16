package com.udn.ntpc.od.core.service.data.apiinfo;

import com.udn.ntpc.od.model.common.ConnectionParam.APIKey_API_METHOD;
import com.udn.ntpc.od.model.domain.TableFields;

import java.util.Map;

public interface JsonAPIDataInfoService {

    /**
     * 原始資料
     * @param url
     * @param method
     * @param params
     * @return
     */
	String apiData(String url, APIKey_API_METHOD method, Object... params);

	/**
	 * 先將原始資料轉成 JSON 格式後，再取得每一階層的欄位清單
	 * @param data
	 * @return
	 */
	Map<String, TableFields> getFieldConfigs(String data);

	Map<String, TableFields> getFieldConfigsFromJson(String data);
	
	String convertToJsonList(String data, String rootPath);
	
//	String convertToJsonList(String data);
	
	String convertToJson(String data);
}
