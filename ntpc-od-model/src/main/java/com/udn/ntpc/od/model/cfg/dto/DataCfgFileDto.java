package com.udn.ntpc.od.model.cfg.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataCfgFileDto implements Serializable {
    private static final long serialVersionUID = 205636823421121150L;

    private String oid;

    private String name;

    private long size;

    private String dataCfgOid;

}
