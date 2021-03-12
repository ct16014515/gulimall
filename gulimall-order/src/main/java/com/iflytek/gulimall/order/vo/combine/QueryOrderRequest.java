package com.iflytek.gulimall.order.vo.combine;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 *    合单下单-JS支付API
 *  * 参考文档：https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/combine/chapter3_3.shtml
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryOrderRequest extends BasePayRequest {

    private static final long serialVersionUID = -8100791351891543014L;

    /**
     * 合单商户订单号
     *  合单支付总订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一 。
     * 示例值：P20150806125346
     */
    @JSONField(name = "combine_out_trade_no")
    private String combineOutTradeNo;


    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public int getReadTimeoutMs() {
        return 0;
    }

    @Override
    public int getConnectTimeoutMs() {
        return 0;
    }

    @Override
    public String getRequestData() {
        return null;
    }
}