package com.udn.ntpc.od.model.domain;

import com.udn.ntpc.od.model.common.ADDR_FIELDS;
import com.udn.ntpc.od.model.common.FIELD_TYPES;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Data table field schema
 *
 */
public class TableFields extends ArrayList<TableField> {
    public static final TableFields EMPTY_FIELDS = new TableFields();
    
    private static final long serialVersionUID = 2829402983327769485L;
    
    private String tableName;
/*
    @Override
    public boolean contains(Object o) {
        if (o != null)
            for (TableField f: this)
                if (f.getId().equals(o))
                    return true;
        return false;
    }
*/    
    public boolean containsById(String id) {
        if (StringUtils.isNotBlank(id)) {
            for (TableField f: this) {
                if (StringUtils.equalsIgnoreCase(f.getId(), id)) {
                    return true;
                }
            }
        }
        return false;
    }
/*    
    @Override
    public int indexOf(Object o) {
        if (o != null)
            for (int idx = 0; idx < size(); idx++) {
                TableField f = get(idx);
                if (f.getId().equals(o))
                    return idx;
            }
        return -1;
    }
*/    
    public int indexOfById(String id) {
        if (StringUtils.isNotBlank(id)) {
            for (int idx = 0; idx < size(); idx++) {
                TableField f = get(idx);
                if (StringUtils.equalsIgnoreCase(f.getId(), id)) {
                    return idx;
                }
            }
        }
        return -1;
    }
    
    public String[] getNames() {
        if (size() == 0)
            return new String[]{};
        String[] result = new String[size()];
        for (int idx = 0; idx < size(); idx++)
            result[idx] = get(idx).getId();
        return result;
    }
    
    public String toString(){
    	String selectedFields = Arrays.toString(getNames());
    	return selectedFields.replace("[","").replace("]","");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 是否有地址欄位
     * @return
     */
    public boolean hasAddressField() {
        for (TableField f: this) {
            if (StringUtils.equalsIgnoreCase(f.getType(), FIELD_TYPES.ADDRESS.getValue()) ||
                    StringUtils.equalsIgnoreCase(f.getOriginalType(), FIELD_TYPES.ADDRESS.getValue()))
                return true;
        }
        return false;
    }
    
    /**
     * 取得地址欄位
     * @return
     */
    public TableField getAddressField() {
        for (TableField f: this) {
            if (StringUtils.equalsIgnoreCase(f.getType(), FIELD_TYPES.ADDRESS.getValue()) ||
                    StringUtils.equalsIgnoreCase(f.getOriginalType(), FIELD_TYPES.ADDRESS.getValue()))
                return f;
        }
        return null;
    }
    
    /**
     * 移除座標欄位
     */
    public void removeCoordinateDataFields() {
        Iterator<TableField> iter = this.iterator();
        while (iter.hasNext()) {
            try {
                TableField f = iter.next();
                ADDR_FIELDS.resovleByValue(f.getId());
                iter.remove();
            } catch(EnumConstantNotPresentException e) {
            }
        }
    }

    /**
     * 是否有 _id 欄位
     * @return
     */
    public boolean hasIdField() {
        return containsById("_id");
    }
    
    /**
     * 移除 _id 欄位
     */
    public void removeIdField() {
        int index = indexOfById("_id");
        if (index > -1) {
            remove(index);
        }
    }
    
}
