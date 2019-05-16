package com.udn.ntpc.od.model.common;

/**
 * 資料設定的操作行為類型
 */
public enum ActionType {
    /**
     * 上架=ENABLE
     */
    ENABLE(1),
    /**
     * 修改=EDIT
     */
    EDIT(2),
    /**
     * 下架=DISABLE
     */
    DISABLE(3);
    private int actionType;

    private ActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getActionType() {
        return this.actionType;
    }
}
