package com.udn.ntpc.od.core.service.cfg;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;

public interface DataFieldCfgService extends CustomService<DataFieldCfg, String> {

    /**
     * 用 dataCfgOid 查 and order by fieldOrder
     * @param dataCfgOid
     * @return
     */
    DataFieldCfgs findByDataCfgOid(String dataCfgOid);

    /**
     * 用 dataSetOid 查 and order by fieldOrder
     * @param dataCfgOid
     * @return
     */
    DataFieldCfgs findByDataSetOid(String dataCfgOid);
}
