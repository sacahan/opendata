package com.udn.ntpc.od.model.system.repository;

import com.udn.ntpc.od.model.repository.CustomRespository;
import com.udn.ntpc.od.model.system.domain.CoordinateMapping;
import org.springframework.data.jpa.repository.Query;

public interface CoordinateMappingRepository extends CustomRespository<CoordinateMapping, String> {

    @Query("from CoordinateMapping m where dataCfgOid = (select oid from DataCfg cfg where dataSetOid = ?1) ")
    CoordinateMapping findByDataSetOid(String dataSetOid);

}
