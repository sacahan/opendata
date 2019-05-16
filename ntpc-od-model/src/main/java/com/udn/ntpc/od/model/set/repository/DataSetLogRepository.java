package com.udn.ntpc.od.model.set.repository;

import com.udn.ntpc.od.model.repository.CustomRespository;
import com.udn.ntpc.od.model.set.domain.DataSetLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Repository
public interface DataSetLogRepository extends CustomRespository<DataSetLog, String> {

    @Query("select new DataSetLog(max(log.oid) as oid, log.dataSetOid, log.performAction, max(log.dataSetName) as dataSetName, max(log.createDate) as createDate, dataSet) "
         + "from DataSetLog log "
         + "left join log.dataSet as dataSet " // for lazy loading not working on group by
         + "where log.createDate >= ?1 "
         + "group by log.dataSetOid, dataSet.oid, log.performAction "
         + "order by createDate ")
    List<DataSetLog> findByCreateDateAfterOrderByCreateDate(@Temporal(TemporalType.TIMESTAMP) Date createDate);

}
