package com.udn.ntpc.od.model.common;

public enum FIELD_TYPES {
    COMMON("common"),
    NUMBER("number"),
    ADDRESS("address"),
    ;

    private final String value;
    private FIELD_TYPES(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static FIELD_TYPES resovleByValue(String value) {
        for (FIELD_TYPES v: values()) {
            if (v.getValue().toUpperCase().equalsIgnoreCase(value.toUpperCase())) {
                return v;
            }
        }
        throw new EnumConstantNotPresentException(FIELD_TYPES.class, value);
    }

}
