package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataCfgTableInfo;
import com.udn.ntpc.od.model.repository.CustomRespository;

/**
 * 結構化資料轉入相關資訊
 */
public interface DataCfgTableInfoRepository extends CustomRespository<DataCfgTableInfo, String> {
    
    DataCfgTableInfo findByDataCfgOid(String dataCfgOid);
    
}
