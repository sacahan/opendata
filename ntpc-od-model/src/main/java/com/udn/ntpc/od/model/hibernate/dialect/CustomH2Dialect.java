package com.udn.ntpc.od.model.hibernate.dialect;

import org.hibernate.dialect.H2Dialect;

public class CustomH2Dialect extends H2Dialect {

    /**
     * 為了配合 GUID 所以轉成大寫
     * @return
     */
    @Override
    public String getSelectGUIDString() {
        return "select upper(uuid())";
    }

}
