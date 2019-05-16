package com.udn.ntpc.od.model.category.repository;

import com.udn.ntpc.od.model.category.domain.DataCategory;
import com.udn.ntpc.od.model.repository.CustomRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataCategoryRepository extends CustomRespository<DataCategory, String> {

}
