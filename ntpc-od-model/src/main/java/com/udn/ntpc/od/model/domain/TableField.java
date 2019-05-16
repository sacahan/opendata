package com.udn.ntpc.od.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data table field schema
 *
 */
@NoArgsConstructor
@Data
public class TableField implements Serializable {
	private static final long serialVersionUID = 8405269098716586753L;
	private String id;
	private String type;
	private String path;
	private String previewValue;
	private String originalType;

    public TableField(String id, String path, String previewValue) {
    	this.id = id;
    	this.path = path;
    	this.previewValue = previewValue;
    	this.originalType = "String";
    }
    
    public TableField(String id, String type, String path, String previewValue, String originalType) {
    	this.id = id;
    	this.type = type;
    	this.path = path;
    	this.previewValue = previewValue;
    	this.originalType = originalType;
    }

}
