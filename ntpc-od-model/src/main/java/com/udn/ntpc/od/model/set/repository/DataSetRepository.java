package com.udn.ntpc.od.model.set.repository;

import com.udn.ntpc.od.model.repository.CustomRespository;
import com.udn.ntpc.od.model.set.domain.DataSet;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSetRepository extends CustomRespository<DataSet, String> {

}
