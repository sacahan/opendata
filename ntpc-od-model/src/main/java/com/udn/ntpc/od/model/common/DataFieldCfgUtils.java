package com.udn.ntpc.od.model.common;

import com.udn.ntpc.od.model.cfg.domain.IDataFieldCfg;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataFieldCfgUtils {

    /**
     * 是否有地址欄位
     * @param dataFieldCfgs
     * @return boolean
     */
    public static boolean hasAddressField(Set<? extends IDataFieldCfg> dataFieldCfgs) {
/*
        for (IDataFieldCfg f: dataFieldCfgs) {
            if (StringUtils.equalsIgnoreCase(f.getFieldType(), FIELD_TYPES.ADDRESS.getValue())) {
                return true;
            }
        }
        return false;
*/
        return dataFieldCfgs.stream()
                .filter(f -> StringUtils.equalsIgnoreCase(f.getFieldType(), FIELD_TYPES.ADDRESS.getValue()))
                .findFirst()
                .isPresent();
    }

    /**
     * 取得地址欄位
     * @param dataFieldCfgs
     * @param <T>
     * @return
     */
    public static <T extends IDataFieldCfg> T getAddressField(Set<? extends IDataFieldCfg> dataFieldCfgs) {
/*
        for (IDataFieldCfg f: dataFieldCfgs) {
            if (StringUtils.equalsIgnoreCase(f.getFieldType(), FIELD_TYPES.ADDRESS.getValue())) {
                return (T) f;
            }
        }
        return null;
*/
        return dataFieldCfgs.stream()
                .filter(f -> StringUtils.equalsIgnoreCase(f.getFieldType(), FIELD_TYPES.ADDRESS.getValue()))
                .findFirst().map(f -> (T) f)
                .orElseGet(null);
    }
    
    /**
     * 移除座標欄位
     */
    public static void removeCoordinateDataFields(Set<? extends IDataFieldCfg> dataFieldCfgs) {
        Iterator<? extends IDataFieldCfg> iter = dataFieldCfgs.iterator();
        while (iter.hasNext()) {
            try {
                IDataFieldCfg f = iter.next();
                ADDR_FIELDS.resovleByValue(f.getFieldName());
                iter.remove();
            } catch(EnumConstantNotPresentException e) {
            }
        }
    }

}
