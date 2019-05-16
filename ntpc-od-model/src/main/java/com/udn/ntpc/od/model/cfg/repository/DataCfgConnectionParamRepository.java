package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataCfgConnectionParam;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataCfgConnectionParamRepository extends CustomRespository<DataCfgConnectionParam, String> {
    
    @Query("select params from DataCfgConnectionParam params " +
           "where exists ( "+ 
           "    select '' from DataCfg dataCfg " +
           "    where dataCfg.dataSetOid = ?1 and params.dataCfgOid = dataCfg.oid" +
           ")")
    List<DataCfgConnectionParam> findByDataSetOid(String dataSetOid);
    
    List<DataCfgConnectionParam> findByDataCfgOid(String dataCfgOid);
    
}
