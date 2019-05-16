package com.udn.ntpc.od.common.variables;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface DEFAULT_SETTINGS {

    Sort DEFAULT_DATA_SORT = null;
    
    /**
     * 每頁 30 筆
     */
    Pageable DEFAULT_DATA_PAGING = PageRequest.of(0, 30);
    
    /**
     * 每頁 2,000 筆
     */
    Pageable DEFAULT_DATA_API_PAGING = PageRequest.of(0, 2000);
    
}
