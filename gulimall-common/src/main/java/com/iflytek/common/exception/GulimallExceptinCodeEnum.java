package com.iflytek.common.exception;


/**
 * 错误码和错误信息定义
 * 1.错误码定义为5位数字
 * 2.前两位表示业务场景,最后三位表示错误码.例如10000,10表示通用,000:系统未知异常
 * 3.维护错误码需要维护错误描述,定义为枚举类型
 * 错误码表:
 * 10:通用
 * 001:参数校验异常
 * 11:商品
 * 12:订单
 * 13:购物车
 * 14:物流
 */
public enum GulimallExceptinCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALIDATE_EXCEPTION(10001, "参数校验异常"),
    PRODUCT_UP_ERROR(11000, "商品上架异常");


    private Integer code;
    private String message;

    GulimallExceptinCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
