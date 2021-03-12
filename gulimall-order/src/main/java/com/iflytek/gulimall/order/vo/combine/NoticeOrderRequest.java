package com.iflytek.gulimall.order.vo.combine;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 *    支付通知
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
public class NoticeOrderRequest extends BasePayRequest {

    private static final long serialVersionUID = -8100791351891543014L;

    /**
     *  通知ID	id	string(32)
     *  是	通知的唯一ID
     *  示例值：EV-2018022511223320873
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 通知创建时间
     * create_time	string(16)
     * 是	通知创建的时间，格式为yyyyMMddHHmmss
 *     示例值：20180225112233
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 通知类型	event_type	string(32)
     * 是	通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS
     *    示例值：TRANSACTION.SUCCESS
     */
    @JSONField(name = "event_type")
    private String eventType;

    /**
     *  通知数据类型	resource_type	string(32)
     *  通知的资源数据类型，支付成功通知为encrypt-resource
     *    示例值：encrypt-resource
     */
    @JSONField(name = "resource_type")
    private String resourceType;

    /**
     * +知数据	resource	object	是	通知资源数据
     *     json格式，见示例
     */
    @JSONField(name = "resource")
    private Resource resource;


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