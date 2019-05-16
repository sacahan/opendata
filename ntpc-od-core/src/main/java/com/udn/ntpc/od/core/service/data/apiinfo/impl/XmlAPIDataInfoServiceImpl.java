package com.udn.ntpc.od.core.service.data.apiinfo.impl;

import com.udn.ntpc.od.common.util.JsonUtils;
import com.udn.ntpc.od.core.service.data.apiinfo.XmlAPIDataInfoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class XmlAPIDataInfoServiceImpl extends AbstractAPIDataInfoServiceImpl implements XmlAPIDataInfoService {

	@Override
	public String convertToJson(String data) {
		return JsonUtils.readXmlToJson(data);
	}

	@Override
	public String convertToJsonList(String data, String rootPath) {
		return JsonUtils.readXmlToJsonList(data, rootPath);
	}

	@Override
	protected List<MediaType> getMediaTypes() {
	    return Arrays.asList(MediaType.TEXT_XML, MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_XML);
	}

}
