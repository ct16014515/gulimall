package com.iflytek.common.utils;

import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResultBody<T> {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private T data;

    public ResultBody() {
    }

    public ResultBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultBody(GulimallExceptinCodeEnum e, T data) {
        this.code = e.getCode();
        this.msg = e.getMessage();
        this.data = data;
    }

}
