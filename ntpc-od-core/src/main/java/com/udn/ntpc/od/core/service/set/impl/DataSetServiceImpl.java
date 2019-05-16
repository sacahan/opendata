package com.udn.ntpc.od.core.service.set.impl;

import com.udn.ntpc.od.core.service.impl.AbstractCustomServiceImpl;
import com.udn.ntpc.od.core.service.set.DataSetService;
import com.udn.ntpc.od.model.set.domain.DataSet;
import com.udn.ntpc.od.model.set.repository.DataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataSetServiceImpl extends AbstractCustomServiceImpl<DataSet, String> implements DataSetService {

    @Autowired
    private DataSetRepository repository;

    @Override
    public DataSet getParentDataSet(String dataSetOid) {
        DataSet dataSet = getOne(dataSetOid);
        return dataSet.getParentDataSet();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncParentDataSetEnableStatus(String dataSetOid) {
        DataSet dataSet = getOne(dataSetOid);
        if (dataSet.isParent()) {
            boolean isEnabled = dataSet.isEnable();
            boolean isPublic = dataSet.isPublic();
            for (DataSet child: dataSet.getChildDataSets()) {
                child.setEnable(isEnabled);
                child.setPublic(isPublic);
                save(child);
            }
        }
    }
/*
    @Override
    public void enableDataSet(String dataSetOid) {
        enableDataSet(dataSetOid, DATA_SET_STATUS.ENABLED);
    }
    
    @Override
    public void disableDataSet(String dataSetOid) {
        enableDataSet(dataSetOid, DATA_SET_STATUS.DISABLED);
    }

    private void enableDataSet(String dataSetOid, boolean isEnabled) {
        DataSet dataSet = findOne(dataSetOid);
        dataSet.setEnable(isEnabled);
        if (dataSet.isParent()) {
            for (DataSet child: dataSet.getChildDataSets()) {
                child.setEnable(isEnabled);
                save(child);
            }
        }
        save(dataSet);
    }
*/

}
