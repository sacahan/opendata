package com.udn.ntpc.od.model.cfg.domain;

import java.util.Date;

public interface IDataCfg {

    String getOid();

    String getName();

    String getDescription();

    boolean isStructured();

    boolean isEnable();

    boolean isPublic();

    String getNoPublicReason();

    Date getStartTime();

    Date getEndTime();

//    List<? extends IDataFieldCfg> getDataFieldCfgs();
}
