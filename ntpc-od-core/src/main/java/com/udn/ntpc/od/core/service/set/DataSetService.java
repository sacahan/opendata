package com.udn.ntpc.od.core.service.set;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.set.domain.DataSet;

public interface DataSetService extends CustomService<DataSet, String> {
    
    DataSet getParentDataSet(String dataSetOid);
    
    void syncParentDataSetEnableStatus(String dataSetOid);
/*
    void enableDataSet(String dataSetOid);
    
    void disableDataSet(String dataSetOid);
*/
}
