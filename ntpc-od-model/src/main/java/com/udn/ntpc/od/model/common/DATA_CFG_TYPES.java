package com.udn.ntpc.od.model.common;

public enum DATA_CFG_TYPES {

    /**
     * 資料檔
     */
    DATA("0"),
    /**
     * 影音檔
     */
    MEDIA("1"),
    /**
     * 圖片檔
     */
    IMAGE("2"),
    /**
     * PDF
     */
    PDF("3"),
    /**
     * 地理圖資
     */
    KML("4")
    ;

    private final String value;
    private DATA_CFG_TYPES(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static DATA_CFG_TYPES resovleByValue(String value) {
        for (DATA_CFG_TYPES v: values()) {
            if (v.getValue().toUpperCase().equalsIgnoreCase(value.toUpperCase())) {
                return v;
            }
        }
        throw new EnumConstantNotPresentException(DATA_CFG_TYPES.class, value);
    }

}
