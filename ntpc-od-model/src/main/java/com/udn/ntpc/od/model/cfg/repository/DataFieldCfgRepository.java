package com.udn.ntpc.od.model.cfg.repository;

import com.udn.ntpc.od.model.cfg.domain.DataFieldCfg;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataFieldCfgRepository extends CustomRespository<DataFieldCfg, String> {
    
    List<DataFieldCfg> findByDataCfgOidOrderByFieldOrder(String dataCfgOid);
    
    @Query("select fieldCfgs from DataFieldCfg fieldCfgs " +
           "where " +
           "    exists (" +
           "        select '' from DataCfg cfg where cfg.dataSetOid = ?1 and fieldCfgs.dataCfgOid = cfg.oid" +
           "    ) " +
           "order by fieldCfgs.fieldOrder")
    List<DataFieldCfg> findByDataSetOidOrderByFieldOrder(String dataSetOid);

}
