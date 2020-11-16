package com.iflytek.gulimall.mq.enume;

public enum MqMessageEnum {
    CREATE_NEW(1, "新建"),
    ERROR_ARRIVE(2, "错误抵达"),
    OK_ARRIVE(3, "已抵达");

    private Integer code;
    private String msg;

    MqMessageEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
