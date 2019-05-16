package com.udn.ntpc.od.core.service.system;

import com.udn.ntpc.od.core.service.CustomService;
import com.udn.ntpc.od.model.system.domain.CoordinateMapping;

public interface CoordinateMappingService extends CustomService<CoordinateMapping, String> {

    CoordinateMapping findByDataSetOid(String dataSetOid);
    
}
