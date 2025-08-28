package com.ruoyi.common.enums;

public enum BookTagStatus {
    ACTIVE(1),
    INACTIVE(0);
    private final int status;
    BookTagStatus(int status){
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
