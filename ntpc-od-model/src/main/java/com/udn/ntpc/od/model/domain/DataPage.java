package com.udn.ntpc.od.model.domain;

import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import org.springframework.data.domain.Page;

public interface DataPage<T> extends Page<T> {
    
    /**
     * 所有欄位資訊
     * @return
     */
    TableFields getFields();

    /**
     * 要顯示的欄位資訊
     * @return
     */
    DataFieldCfgsDto getDataFieldCfgs();
    
}
