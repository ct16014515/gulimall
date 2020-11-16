package com.iflytek.gulimall.common.utils;

import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import lombok.Builder;
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
        this.code = 0;
        this.msg = "success";
    }

    public ResultBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public ResultBody( T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }
    public ResultBody(GulimallExceptinCodeEnum e, T data) {
        this.code = e.getCode();
        this.msg = e.getMessage();
        this.data = data;
    }

    public ResultBody(GulimallExceptinCodeEnum e) {
        this.code = e.getCode();
        this.msg = e.getMessage();
    }


}
