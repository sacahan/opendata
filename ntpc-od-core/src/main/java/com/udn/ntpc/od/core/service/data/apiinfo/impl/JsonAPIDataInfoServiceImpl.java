package com.udn.ntpc.od.core.service.data.apiinfo.impl;

import com.udn.ntpc.od.common.util.JsonUtils;
import com.udn.ntpc.od.core.service.data.apiinfo.JsonAPIDataInfoService;
import com.udn.ntpc.od.model.domain.TableFields;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class JsonAPIDataInfoServiceImpl extends AbstractAPIDataInfoServiceImpl implements JsonAPIDataInfoService {

    @Override
    public Map<String, TableFields> getFieldConfigs(String data) {
        return getFieldConfigsFromJson(data);
    }

	@Override
	public String convertToJson(String data) {
		return data;
	}

	@Override
	public String convertToJsonList(String data, String rootPath) {
        return JsonUtils.readJsonToJsonList(data, rootPath);
	}

    @Override
    protected List<MediaType> getMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON);
    }

}
