package com.udn.ntpc.od.model.common;

public enum ADDR_FIELDS {
    
    TWD97_X("twd97X"),
    TWD97_Y("twd97Y"),
    WGS84A_X("wgs84aX"),
    WGS84A_Y("wgs84aY"),
    ;

    private final String value;
    private ADDR_FIELDS(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static ADDR_FIELDS resovleByValue(String value) {
        for (ADDR_FIELDS v: values()) {
            if (v.getValue().toUpperCase().equalsIgnoreCase(value.toUpperCase())) {
                return v;
            }
        }
        throw new EnumConstantNotPresentException(ADDR_FIELDS.class, value);
    }

}
