package com.udn.ntpc.od.model.cfg.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataCfgZipFileDto implements Serializable {
	private String oid;

	private String zipFileName;

	private String sourceType;

	private long size;

	private String md5;
	
}
