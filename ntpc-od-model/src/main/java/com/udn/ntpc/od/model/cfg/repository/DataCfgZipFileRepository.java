package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.model.repository.CustomRespository;

/**
 * Zip 檔案
 */
public interface DataCfgZipFileRepository extends CustomRespository<DataCfgZipFile, String> {
	
	DataCfgZipFile findByDataCfgOid(String dataCfgOid);

}
