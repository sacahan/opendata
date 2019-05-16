package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.common.util.FileUtils;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.cfg.DataCfgZipFileService;
import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.model.cfg.repository.DataCfgZipFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class DataCfgZipFileServiceImpl extends AbstractCustomServiceImpl<DataCfgZipFile, String> implements DataCfgZipFileService {

    @Autowired 
    private DataCfgZipFileRepository repository;
    
    @Autowired
    private DataCfgService dataCfgService;
    
    @Override
    public DataCfgZipFile saveZipFile(String dataSetOid, File zipFile, String sourceType) throws IOException {
        DataCfgZipFile dataCfgZipFile = new DataCfgZipFile();
        return updateZipFile(dataCfgZipFile, dataSetOid, zipFile, sourceType);
    }
    
    @Override
    public DataCfgZipFile updateZipFile(String dataSetOid, File zipFile, String sourceType) throws IOException {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        
        String baseFileName = dataCfg.getName();
        String zipFileName = baseFileName + ".zip";
        
        for (DataCfgZipFile f: dataCfg.getDataCfgZipFiles()) {
            if (StringUtils.equalsIgnoreCase(sourceType, f.getSourceType()) &&
                    StringUtils.equalsIgnoreCase(zipFileName, f.getZipFileName())) {
                return updateZipFile(f, dataSetOid, zipFile, sourceType);
            }
        }
        
        return saveZipFile(dataSetOid, zipFile, sourceType);
    }
    
    private DataCfgZipFile updateZipFile(DataCfgZipFile dataCfgZipFile, String dataSetOid, File zipFile, String sourceType) throws IOException {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        String baseFileName = dataCfg.getName();
        String zipFileName = baseFileName + ".zip";

        dataCfgZipFile.setContentFile(zipFile);
        dataCfgZipFile.setMd5(FileUtils.md5Hex(zipFile));
        dataCfgZipFile.setDataCfg(dataCfg);
        dataCfgZipFile.setSourceType(sourceType);
        dataCfgZipFile.setZipFileName(zipFileName);
        return save(dataCfgZipFile);
    }
    
    private DataCfg findDataCfgByDataSetOid(String dataSetOid) {
        DataCfg dataCfg = dataCfgService.findByDataSetOid(dataSetOid);
        if (dataCfg == null) {
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        }
        return dataCfg;
    }

}
