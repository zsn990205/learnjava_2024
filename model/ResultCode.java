package com.bite.book.model;

public enum ResultCode {
    SUCCESS(0),
    FAIL(-1),
    UNLOGIN(2)
    ;
    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;

}
