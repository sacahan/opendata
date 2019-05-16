package com.udn.ntpc.od.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udn.ntpc.od.common.exception.OpdException;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

@NoArgsConstructor
@JsonIgnoreProperties(value={"tableName"}, ignoreUnknown=false)
public class TableSchema implements Serializable {
    private static final long serialVersionUID = 5566875118126253941L;
    
    @JsonProperty("data_oid")
    private String dataOid;
    private TableFields fields = new TableFields();
    
    public String getDataOid() {
        return dataOid;
    }

    public void setDataOid(String dataOid) {
        this.dataOid = dataOid;
    }

    public TableFields getFields() {
        return fields;
    }

    public void setFields(TableFields fields) {
        this.fields = fields;
    }

    public String getTableName() {
        return this.fields.getTableName();
    }

    public void setTableName(String tableName) {
        this.fields.setTableName(tableName);
    }
    
    public void addField(TableField field) {
        fields.add(field);
    }
    
    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new OpdException(e.getMessage());
        }
    }
    
}
