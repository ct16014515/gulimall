package com.iflytek.gulimall.common.model.enume;

public enum OrderStatusEnum {
    CREATE_NEW(0, "待付款"),
    PAYED(1, "已付款"),
    SENDED(2, "已发货"),
    RECIEVED(3, "已完成"),
    CANCLED(4, "已取消"),
    SERVICING(5, "售后中"),
    SERVICED(6, "售后完成");
    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsg(Integer code) {
        if (OrderStatusEnum.CREATE_NEW.code.equals(code)) {
            return OrderStatusEnum.PAYED.msg;
        } else if (OrderStatusEnum.PAYED.code.equals(code)) {
            return OrderStatusEnum.PAYED.msg;
        } else if (OrderStatusEnum.RECIEVED.code.equals(code)) {
            return OrderStatusEnum.RECIEVED.msg;
        } else if (OrderStatusEnum.CANCLED.code.equals(code)) {
            return OrderStatusEnum.CANCLED.msg;
        } else if (OrderStatusEnum.SERVICING.code.equals(code)) {
            return OrderStatusEnum.SERVICING.msg;
        } else {
            return OrderStatusEnum.SERVICED.msg;
        }

    }


}



