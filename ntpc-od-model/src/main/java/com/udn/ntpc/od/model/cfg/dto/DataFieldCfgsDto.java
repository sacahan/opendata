package com.udn.ntpc.od.model.cfg.dto;

import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfgApply;
import com.udn.ntpc.od.model.cfg.domain.IDataFieldCfg;
import com.udn.ntpc.od.model.common.ADDR_FIELDS;
import com.udn.ntpc.od.model.common.DataFieldCfgUtils;
import com.udn.ntpc.od.model.common.FIELD_TYPES;
import com.udn.ntpc.od.model.domain.ValueObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 資料設定用的欄位檢核DTO
 */
@Component
@Slf4j
public class DataFieldCfgsDto extends ArrayList<DataFieldCfgDto> implements ValueObject {

    public static final DataFieldCfgsDto EMPTY_FIELD_CFGS = new DataFieldCfgsDto();
    
    private static final long serialVersionUID = -6745595232201346972L;

    @PostConstruct
    private void postConstruct() {
        log.debug(getClass().getName() + " constructed");
    }

    /**
     * check id
     */
    @Override
    public boolean contains(Object o) {
        if (o != null) {
            for (DataFieldCfgDto f : this) {
                if (f.getFieldName().equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * check id
     */
    @Override
    public int indexOf(Object o) {
        if (o != null) {
            for (int idx = 0; idx < size(); idx++) {
                DataFieldCfgDto f = get(idx);
                if (f.getFieldName().equals(o)) {
                    return idx;
                }
            }
        }
        return -1;
    }
    
    public String[] getNames() {
        if (size() == 0) {
            return new String[]{};
        }
        String[] result = new String[size()];
        for (int idx = 0; idx < size(); idx++) {
            result[idx] = get(idx).getFieldName();
        }
        return result;
    }

    public void copyFrom(Set<? extends IDataFieldCfg> dataFieldCfgs) {
        clear();
        for (IDataFieldCfg dataFieldCfg: dataFieldCfgs) {
            DataFieldCfgDto dto = new DataFieldCfgDto();
            dto.copyFrom(dataFieldCfg);
            add(dto);
        }
    }

    public DataFieldCfgs transformToDataFieldCfgs() {
        Set<DataFieldCfg> dataFieldCfgList = transformToDataFieldCfgs(DataFieldCfg.class);
        DataFieldCfgs dataFieldCfgs = new DataFieldCfgs();
        dataFieldCfgs.addAll(dataFieldCfgList);
        return dataFieldCfgs;
    }
    
    public DataFieldCfgApplies transformToDataFieldCfgApplies() {
        Set<DataFieldCfgApply> dataFieldCfgList = transformToDataFieldCfgs(DataFieldCfgApply.class);
        DataFieldCfgApplies dataFieldCfgApplies = new DataFieldCfgApplies();
        dataFieldCfgApplies.addAll(dataFieldCfgList);
        return dataFieldCfgApplies;
    }
    
    private <T extends IDataFieldCfg> Set<T> transformToDataFieldCfgs(Class<T> clazzDataFieldCfg) {
        Set<T> dataFieldCfgs = new LinkedHashSet<>();

        boolean hasAddressField = false;
        for (DataFieldCfgDto dataFieldCfgDto: this) {
            if (!hasAddressField && dataFieldCfgDto.getFieldType().equalsIgnoreCase(FIELD_TYPES.ADDRESS.getValue())) {
                hasAddressField = true;
            }
            try {
                T dataFieldCfg = clazzDataFieldCfg.newInstance();
                dataFieldCfgDto.transformTo(dataFieldCfg);
                dataFieldCfgs.add(dataFieldCfg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        if (hasAddressField) {
            DataFieldCfgUtils.removeCoordinateDataFields(dataFieldCfgs);
            dataFieldCfgs.addAll(genCoordinateDataFields(clazzDataFieldCfg));
        }

        return dataFieldCfgs;
    }

    private <T extends IDataFieldCfg> List<T> genCoordinateDataFields(Class<T> clazz) {
        List<T> dataFieldCfgs = new ArrayList<T>();
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class, String.class, BigInteger.class, String.class, String.class, String.class);
            {
                T dataFieldCfg = constructor.newInstance(ADDR_FIELDS.TWD97_X.getValue(), "twd97緯度", BigInteger.valueOf(dataFieldCfgs.size()),
                                                         FIELD_TYPES.COMMON.getValue(), "skip", "");
                dataFieldCfgs.add(dataFieldCfg);
            }
            {
                T dataFieldCfg = constructor.newInstance(ADDR_FIELDS.TWD97_Y.getValue(), "twd97經度", BigInteger.valueOf(dataFieldCfgs.size()),
                                                         FIELD_TYPES.COMMON.getValue(), "skip", "");
                dataFieldCfgs.add(dataFieldCfg);
            }
            {
                T dataFieldCfg = constructor.newInstance(ADDR_FIELDS.WGS84A_X.getValue(), "wgs84a緯度", BigInteger.valueOf(dataFieldCfgs.size()),
                                                         FIELD_TYPES.COMMON.getValue(), "skip", "");
                dataFieldCfgs.add(dataFieldCfg);
            }
            {
                T dataFieldCfg = constructor.newInstance(ADDR_FIELDS.WGS84A_Y.getValue(), "wgs84a經度", BigInteger.valueOf(dataFieldCfgs.size()),
                                                         FIELD_TYPES.COMMON.getValue(), "skip", "");
                dataFieldCfgs.add(dataFieldCfg);
            }
            return dataFieldCfgs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataFieldCfgsDto genDefaultInstance() {
        return this;
    }
    
}
