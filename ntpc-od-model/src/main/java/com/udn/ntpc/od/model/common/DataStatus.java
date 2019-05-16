package com.udn.ntpc.od.model.common;

/**
 * 資料設定的資料狀態
 */
public enum DataStatus {
    /**
     * 申請中=APPLY
     */
    APPLY(1),
    /**
     * 已通過=AGREE
     */
    AGREE(2),
    /**
     * 退回=REFUSE
     */
    REFUSE(3);
    private int dataStatus;

    private DataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getDataStatus() {
        return this.dataStatus;
    }

}
