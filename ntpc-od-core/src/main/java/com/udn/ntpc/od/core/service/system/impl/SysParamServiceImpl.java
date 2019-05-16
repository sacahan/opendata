package com.udn.ntpc.od.core.service.system.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.core.service.system.SysParamService;
import com.udn.ntpc.od.model.system.domain.SysParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysParamServiceImpl extends AbstractCustomServiceImpl<SysParam, String> implements SysParamService {
    private static final String UNIT_CODE_NAME = "unitCode";
    private static final String CHILD_SERIAL_NUMBER_NAME = "childSerialNumber";
    private static final Object locker = new Object();
    
    private static final String COORDINATE_MAPPING_FIELD_NAMES = "coordinate.mapping.fieldNames";

    @Override
    public String parameter(String id) {
        return parameter(id, null);
    }

    @Override
    public String parameter(String id, String defaultValue) {
        SysParam result = getOne(id);
        if (result == null) {
            return defaultValue;
        }
        if (result.getVal() != null) {
            return result.getVal();
        }
        return defaultValue;
    }

    @Override
    public SysParam saveParameter(String id, String value) {
        SysParam param = getOne(id);
        if (param == null) {
            param = new SysParam();
            param.setId(id);
            param.setDispSort(1);
            param.setName(id);
            param.setNote("系統自動產生");
        }
        param.setVal(value);
        return save(param);
    }

    @Override
    public String getUnitCode() {
        return parameter(UNIT_CODE_NAME, "382000000A");
    }

    @Override
    public String genChildSerialNumber() {
        synchronized (locker) {
            String value = parameter(CHILD_SERIAL_NUMBER_NAME);
            if (StringUtils.isBlank(value))
                throw new OpdException(CHILD_SERIAL_NUMBER_NAME, new OpdException(ErrorCodeEnum.ERR_1001000_EXCEPTION));
            int childSerialNumber = Integer.valueOf(value);
            childSerialNumber++;
            saveParameter(CHILD_SERIAL_NUMBER_NAME, String.valueOf(childSerialNumber));
            return getUnitCode() + "-" + String.format("G%05d", childSerialNumber);
        }
    }
    
    @Override
    public String getCurrentChildSerialNumber() {
        String value = parameter(CHILD_SERIAL_NUMBER_NAME);
        if (StringUtils.isBlank(value))
            throw new OpdException(CHILD_SERIAL_NUMBER_NAME, new OpdException(ErrorCodeEnum.ERR_1001000_EXCEPTION));
        int childSerialNumber = Integer.valueOf(value);
        return getUnitCode() + "-" + String.format("G%05d", childSerialNumber);
    }

    @Override
    public String genResourceId(String identifier) {
        return identifier + "-001";
    }

    @Override
    public String genSubResourceIds(String identifier) {
        return identifier + "-002," + identifier + "-003," + identifier + "-004";
    }

    @Override
    public String[] getCoordinateMappingFieldNames() {
        String value = parameter(COORDINATE_MAPPING_FIELD_NAMES);
        if (StringUtils.isBlank(value))
            throw new OpdException(COORDINATE_MAPPING_FIELD_NAMES, new OpdException(ErrorCodeEnum.ERR_1001000_EXCEPTION));
        return value.split(",");
    }

}
