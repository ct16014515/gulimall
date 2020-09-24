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
 * 15:会员
 */
public enum GulimallExceptinCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALIDATE_EXCEPTION(10001, "参数校验异常"),
    PRODUCT_UP_ERROR(11000, "商品上架异常"),
    PRODUCT_SEARCH_ERROR(11001, "搜索关键字不能为空"),
    FEIGN_ERROR(10002, "服务异常,请稍后重试"),
    MEMBER_USERNAME_EXIT_ERROR(15000, "用户名已经存在"),
    MEMBER_PHONE_EXIT_ERROR(15001, "手机号码已经存在"),
    MEMBER_ACCOUNT_NOTEXIT_ERROR(15002, "账号不存在"),
    MEMBER_PASSWORD_ERROR(15003, "用户名不存在"),
    AUTH2_ERROR(15004, "授权失败"),
    WARE_STOCK_ERROR(14001, "库存不足");
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
