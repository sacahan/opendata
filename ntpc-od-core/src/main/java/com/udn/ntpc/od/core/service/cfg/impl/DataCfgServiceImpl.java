package com.udn.ntpc.od.core.service.cfg.impl;

import com.udn.ntpc.od.common.exception.OpdException;
import com.udn.ntpc.od.common.message.ErrorCodeEnum;
import com.udn.ntpc.od.core.service.cfg.DataCfgService;
import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.model.cfg.domain.DataCfg;
import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.cfg.domain.DataCfgZipFile;
import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.cfg.domain.IDataFieldCfg;
import com.udn.ntpc.od.model.cfg.dto.DataCfgAPIConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataCfgConnectionParams;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgs;
import com.udn.ntpc.od.model.cfg.dto.DataFieldCfgsDto;
import com.udn.ntpc.od.model.cfg.repository.DataCfgRepository;
import com.udn.ntpc.od.model.common.ConnectionParam;
import com.udn.ntpc.od.model.common.DataFieldCfgUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class DataCfgServiceImpl extends AbstractCustomServiceImpl<DataCfg, String> implements DataCfgService {

    @Autowired
    private DataCfgRepository repository;

    @Override
    public DataCfg findByDataSetOid(String dataSetOid) {
        return repository.findByDataSetOid(dataSetOid);
    }
    
    @Override
    public Set<DataFieldCfg> getDataFieldConfigs(String dataSetOid) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        return dataCfg.getDataFieldCfgs();
    }

    @Override
    public DataCfg saveDataCfgConnectionParams(String dataSetOid, Map<String, String> params) {
        return saveDataCfgConnectionParams(dataSetOid, new DataCfgConnectionParams(params));
    }
    
    @Override
    public DataCfg saveDataCfgConnectionParams(String dataSetOid, DataCfgConnectionParams params) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);

        DataCfgConnectionParams currentParams = new DataCfgConnectionParams(dataCfg.getDataCfgConnectionParams());
        DataCfgConnectionParam currentPassword = currentParams.get(ConnectionParam.DBKey.DB_PASSWORD);
        DataCfgConnectionParam newPassword = params.get(ConnectionParam.DBKey.DB_PASSWORD);
        if (newPassword == null) {
            params.set(currentPassword);
        }
        dataCfg.getDataCfgConnectionParams().clear();
        flush();
        params.updateDataCfgConnectionParams(dataCfg);
        params.validateParams();
        return save(dataCfg);
    }
    
    @Override
    public DataCfg saveDataCfgConnectionParams(String dataSetOid, DataCfgAPIConnectionParams params) {
    	DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
    	dataCfg.getDataCfgConnectionParams().clear();
    	flush();
    	params.updateDataCfgConnectionParams(dataCfg);
    	params.validateParams();
    	return save(dataCfg);
    }
    
    @Override
    public DataCfg saveDataFieldCfgsDto(String dataSetOid, DataFieldCfgsDto dataFieldCfgsDto) {
        findDataCfgByDataSetOid(dataSetOid);
        
        DataFieldCfgs dataFieldCfgs = dataFieldCfgsDto.transformToDataFieldCfgs();
        
        return saveDataFieldCfgs(dataSetOid, dataFieldCfgs);
    }
    
    @Override
    public DataCfg saveDataFieldCfgs(String dataSetOid, DataFieldCfgs dataFieldCfgs) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        
        verifyAddressField(dataCfg.getDataFieldCfgs(), dataFieldCfgs);
        
        dataCfg.getDataFieldCfgs().clear();
        flush();
        dataFieldCfgs.updateDataCfgFields(dataCfg);
        
        return save(dataCfg);
    }

    @Override
    public void deleteAllCsvCfgZipFile(String dataSetOid) {
        deleteAllCfgZipFilesByType(dataSetOid, "csv");
    }
    
    @Override
    public void deleteAllKmlCfgZipFile(String dataSetOid) {
        deleteAllCfgZipFilesByType(dataSetOid, "kml");
    }

    private DataCfg findDataCfgByDataSetOid(String dataSetOid) {
        DataCfg dataCfg = findByDataSetOid(dataSetOid);
        if (dataCfg == null) {
            throw new OpdException(dataSetOid, new OpdException(ErrorCodeEnum.ERR_2080001_EXCEPTION));
        }
        return dataCfg;
    }
    
    /**
     * 判斷是否有移除 Address 欄位，如果原本有選擇 Address 欄位，但這次沒選擇，則將所有座標欄位移除<br>
     * oriDataFieldCfgs 跟 newDataFieldCfgs 必須在不同 hibernate sessioni scope
     * @param oriDataFieldCfgs 修改 '前' 的欄位資訊
     * @param newDataFieldCfgs 修改 '後' 的欄位資訊
     */
    private void verifyAddressField(Set<? extends IDataFieldCfg> oriDataFieldCfgs, DataFieldCfgs newDataFieldCfgs) {
        if (DataFieldCfgUtils.hasAddressField(oriDataFieldCfgs) && !newDataFieldCfgs.hasAddressField()) {
            newDataFieldCfgs.removeCoordinateDataFields();
        }
    }

    private void deleteAllCfgZipFilesByType(String dataSetOid, String sourceType) {
        DataCfg dataCfg = findDataCfgByDataSetOid(dataSetOid);
        Iterator<DataCfgZipFile> dataCfgZipFiles = dataCfg.getDataCfgZipFiles().iterator();
        while (dataCfgZipFiles.hasNext()) {
            DataCfgZipFile f = dataCfgZipFiles.next();
            if (StringUtils.equalsIgnoreCase(f.getSourceType(), sourceType)) {
                dataCfgZipFiles.remove();
            }
        }
        saveAndFlush(dataCfg);
    }
    
}
