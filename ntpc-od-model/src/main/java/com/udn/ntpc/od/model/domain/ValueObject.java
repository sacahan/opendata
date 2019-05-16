package com.udn.ntpc.od.model.domain;

import java.io.Serializable;

public interface ValueObject extends Serializable {

    /**
     * 產生預設值
     * @return
     */
    Object genDefaultInstance();

}
