package com.udn.ntpc.od.model.cfg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.udn.ntpc.od.model.cfg.domain.IDataFieldCfg;
import com.udn.ntpc.od.model.common.FIELD_TYPES;
import com.udn.ntpc.od.model.domain.ValueObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

/**
 * 資料設定用的欄位檢核DTO
 */
@Component
@JsonIgnoreProperties(value={"checkMethod", "checkRule"}, ignoreUnknown=false)
@NoArgsConstructor
@Data
@Slf4j
public class DataFieldCfgDto implements ValueObject {
    public static final DataFieldCfgDto EMPTY_FIELD_CFG = new DataFieldCfgDto();
    
    private static final long serialVersionUID = -721560860246784770L;

    @PostConstruct
    private void postConstruct() {
        log.debug(getClass().getName() + " constructed");
    }

    // 欄位標準名稱
    private String fieldName;

    // 欄位檢核類型
    private String fieldType;
    
    // 顯示名稱
    private String displayName;
    
    // 欄位路徑
    private String fieldPath;
    
    private BigInteger fieldOrder;

    // 對應檢核方式
    private String checkMethod;

    // 類型檢核規則
    private String checkRule;

    // 欄位公開屬性
    private boolean isPublic;

	public DataFieldCfgDto(String fieldName, String fieldType, String displayName, BigInteger fieldOrder) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.displayName = displayName;
        this.fieldOrder = fieldOrder;
    }

	public DataFieldCfgDto(String fieldName, String fieldType, String displayName, String fieldPath, BigInteger fieldOrder) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.displayName = displayName;
		this.fieldOrder = fieldOrder;
		this.fieldPath = fieldPath;
	}

	public void copyFrom(IDataFieldCfg dataFieldCfg) {
        setFieldName(dataFieldCfg.getFieldName());
        setFieldType(dataFieldCfg.getFieldType());
        setDisplayName(dataFieldCfg.getDisplayName());
        setFieldOrder(dataFieldCfg.getFieldOrder());
        setFieldPath(dataFieldCfg.getFieldPath());
        setCheckMethod(dataFieldCfg.getCheckMethod());
        setCheckRule(dataFieldCfg.getCheckRule());
        setPublic(dataFieldCfg.isPublic());
	}

	public void transformTo(IDataFieldCfg dataFieldCfg) {
	    dataFieldCfg.setFieldName(getFieldName());
	    dataFieldCfg.setFieldType(getFieldType());
	    dataFieldCfg.setDisplayName(getDisplayName());
	    dataFieldCfg.setFieldOrder(getFieldOrder());
	    dataFieldCfg.setFieldPath(getFieldPath());
	    dataFieldCfg.setCheckMethod(getCheckMethod());
        dataFieldCfg.setCheckRule("");
        switch (FIELD_TYPES.resovleByValue(dataFieldCfg.getFieldType())) {
            case COMMON:
                dataFieldCfg.setCheckMethod("skip");
                break;
            case NUMBER:
                dataFieldCfg.setCheckMethod("isNumber");
                break;
            default:
                break;
        }
	    dataFieldCfg.setPublic(isPublic());
	}

    @Override
    public DataFieldCfgDto genDefaultInstance() {
        return EMPTY_FIELD_CFG;
    }

}
