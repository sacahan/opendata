package com.udn.ntpc.od.model.domain;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@NoArgsConstructor
@Data
@ToString
public class Parameter {
    private String name;
    private String type;
    private String value;
    private String originalFilter;
    private String sqlCondition;
    
    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public Object getValue() {
        return getValueByType();
    }
    
    private Object getValueByType() {
        if (value == null) {
            return null;
        }
        try {
            if (StringUtils.equalsIgnoreCase(type, "int")) {
                return Integer.parseInt(value);
            } else if (StringUtils.equalsIgnoreCase(type, "long")) {
                return Long.parseLong(value);
            } else if (StringUtils.equalsIgnoreCase(type, "double")) {
                return Double.parseDouble(value);
            } else if (StringUtils.equalsIgnoreCase(type, "boolean")) {
                return Boolean.parseBoolean(value);
            } else if (StringUtils.equalsIgnoreCase(type, "timestamp")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return format.parseObject(value);
            }
        } catch (NumberFormatException e) {
            // 丟出int、double or long格式錯誤的exception
            throw new OpdException(String.format("type: %s, value: %s", type, value), new OpdException(ErrorCodeEnum.ERR_1001300_EXCEPTION, e));
        } catch (ParseException e) {
            // 丟出日期格式錯誤的exception
            throw new OpdException(String.format("type: %s, value: %s", type, value), new OpdException(ErrorCodeEnum.ERR_1001301_EXCEPTION, e));
        }
        return value;
    }
    
}
