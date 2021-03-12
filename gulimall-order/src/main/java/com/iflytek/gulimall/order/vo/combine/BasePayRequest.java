package com.iflytek.gulimall.order.vo.combine;

import com.alibaba.fastjson.JSON;
import com.iflytek.gulimall.order.vo.BaseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *   微信支付请求参数的基础实体
 *
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public abstract class BasePayRequest extends BaseEntity {

    private static final long serialVersionUID = 2817376899130267585L;


    public abstract String getUrl();

    public abstract int getReadTimeoutMs();

    public abstract int getConnectTimeoutMs();

    public String getRequestData() {
        return JSON.toJSONString(this);
    }
}