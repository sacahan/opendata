package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataCfgFile;
import com.udn.ntpc.od.model.repository.CustomRespository;

/**
 * 手動檔案上傳
 */
public interface DataCfgFileRepository extends CustomRespository<DataCfgFile, String> {
	
    DataCfgFile findByDataCfgOid(String dataCfgOid);

}
