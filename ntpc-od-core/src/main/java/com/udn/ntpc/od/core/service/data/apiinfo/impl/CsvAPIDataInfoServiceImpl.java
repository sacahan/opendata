package com.udn.ntpc.od.core.service.data.apiinfo.impl;

import com.udn.ntpc.od.common.util.JsonUtils;
import com.udn.ntpc.od.core.service.data.apiinfo.CsvAPIDataInfoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CsvAPIDataInfoServiceImpl extends AbstractAPIDataInfoServiceImpl implements CsvAPIDataInfoService {

	@Override
	public String convertToJson(String data) {
		return JsonUtils.readCsvToJson(data);
	}
	
	@Override
	public String convertToJsonList(String data, String rootPath) {
		return convertToJson(data);
	}

    @Override
    protected List<MediaType> getMediaTypes() {
        return Arrays.asList(MediaType.TEXT_PLAIN);
    }

}
