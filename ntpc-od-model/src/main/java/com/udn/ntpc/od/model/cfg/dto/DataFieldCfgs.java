package com.udn.ntpc.od.model.cfg.dto;

import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.common.DataFieldCfgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Slf4j
public class DataFieldCfgs extends LinkedHashSet<DataFieldCfg> {

    private static final long serialVersionUID = -4477025936244950519L;

    @PostConstruct
    private void postConstruct() {
        log.debug(getClass().getName() + " constructed");
    }

    public void updateDataCfgFields(DataCfg dataCfg) {
        for (DataFieldCfg dataFieldCfg: this) {
            dataFieldCfg.setDataCfg(dataCfg);
        }
        
        Set<DataFieldCfg> dataFieldCfgs = dataCfg.getDataFieldCfgs();
        dataFieldCfgs.clear();
//        entityManger.flush();
        dataFieldCfgs.addAll(this);
    }

    public DataFieldCfg firstField() {
        return this.stream().findFirst().orElseGet(null);
    }

    /**
     * 是否有地址欄位
     * @return
     */
    public boolean hasAddressField() {
        return DataFieldCfgUtils.hasAddressField(this);
    }
    
    /**
     * 取得地址欄位
     * @return
     */
    public DataFieldCfg getAddressField() {
        return DataFieldCfgUtils.getAddressField(this);
    }
    
    /**
     * 移除座標欄位
     */
    public void removeCoordinateDataFields() {
        DataFieldCfgUtils.removeCoordinateDataFields(this);
    }

}
