package com.udn.ntpc.od.common.converter;

import com.udn.ntpc.od.common.util.GUID;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 將 String 轉換成 GUID，適用於 url。
 * 
 */
@Component
public class StringToGUIDObjectConverter extends AbstractObjectConverter<String, GUID> {

    @Override
    protected GUID doConvert(String source) {
        return (StringUtils.hasLength(source) ? GUID.fromString(source.trim()) : null);
    }

}
