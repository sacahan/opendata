package com.udn.ntpc.od.core.service.cfg;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;

import java.io.File;
import java.io.IOException;

public interface DataCfgZipFileService extends CustomService<DataCfgZipFile, String> {
    
    /**
     * 新增 DataCfgZipFile
     * @param dataSetOid
     * @param zipFile
     * @param sourceType
     * @return
     * @throws java.io.IOException
     */
    DataCfgZipFile saveZipFile(String dataSetOid, File zipFile, String sourceType) throws IOException;

    /**
     * 更新，若不存在則新增 DataCfgZipFile<br>
     * 因為前台全檔下載需要，所以延用原始 OID
     * @param dataSetOid
     * @param zipFile
     * @param sourceType
     * @return
     * @throws java.io.IOException
     */
    DataCfgZipFile updateZipFile(String dataSetOid, File zipFile, String sourceType) throws IOException;
    
}
