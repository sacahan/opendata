package com.udn.ntpc.od.model.cfg.domain;

import java.math.BigInteger;

public interface IDataFieldCfg {

    String getOid();

    String getFieldName();
    void setFieldName(String fieldName);

    String getDisplayName();
    void setDisplayName(String displayName);

    BigInteger getFieldOrder();
    void setFieldOrder(BigInteger fieldOrder);

    String getFieldType();
    void setFieldType(String fieldType);
    
    String getFieldPath();
    void setFieldPath(String fieldType);

    String getCheckRule();
    void setCheckRule(String checkRule);

    String getCheckMethod();
    void setCheckMethod(String checkMethod);

    boolean isPublic();
    void setPublic(boolean isPublic);

    IDataCfg getDataCfg();
    void setDataCfg(IDataCfg dataCfg);

}
