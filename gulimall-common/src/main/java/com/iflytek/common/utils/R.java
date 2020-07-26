/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.iflytek.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.iflytek.common.exception.GulimallExceptinCodeEnum;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * TODO:在返回值参数封装上不使用map格式,使用对象封装属性方式,方便微服务调用时对象互转 ResultBody<T>
 *
 */
@Data
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public R setData(Object data) {
        put("data", data);
        return this;
    }
    public <T> T getData(TypeReference<T> type) {
        Object data = get("data");
        String s = JSONObject.toJSONString(data);
        T t = JSONObject.parseObject(s, type);
        return t;
    }


    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(GulimallExceptinCodeEnum gulimallExceptinCodeEnum) {
        R r = new R();
        r.put("code", gulimallExceptinCodeEnum.getCode());
        r.put("msg", gulimallExceptinCodeEnum.getMessage());
        return r;
    }


    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
