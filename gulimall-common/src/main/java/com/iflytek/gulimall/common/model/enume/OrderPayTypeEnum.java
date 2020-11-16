package com.iflytek.gulimall.common.model.enume;

public enum OrderPayTypeEnum {
    //支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
    ALI(1, "支付宝"),
    WX(2, "微信");
    private Integer code;
    private String msg;

    OrderPayTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
