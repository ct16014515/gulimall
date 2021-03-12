//package com.iflytek.gulimall.order.vo.combine;
//
//import com.alibaba.fastjson.annotation.JSONField;
//
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import java.util.List;
//
///**
// * <p>
// *
// *    合单下单-JS支付API
// *  * 参考文档：https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pay/combine/chapter3_2.shtml
// * </p>
// *
// *  最终请求例子
// *  {
// *  "combine_out_trade_no": "1217752501201407033233368018",
// *  "combine_mchid": "1230000109",
// *  "combine_appid": "wxd678efh567hg6787",
// *  "scene_info": {
// *    "device_id": "POS1:1",
// *    "payer_client_ip": "14.17.22.32"
// *  },
// *  "sub_orders": [
// *    {
// *      "mchid": "1230000109",
// *      "attach": "深圳分店",
// *      "amount": {
// *        "total_amount": 10,
// *        "currency": "CNY"
// *      },
// *      "out_trade_no": "20150806125346",
// *      "sub_mchid": "1900000109",
// *      "detail": "",
// *      "description": "腾讯充值中心-QQ会员充值"
// *    }
// *  ],
// *  "combine_payer_info": {
// *    "openid": "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
// *  },
// *  "time_start": "2018-06-08T10:34:56+08:00",
// *  "time_expire": "2018-06-08T10:34:56+08:00",
// *  "notify_url": "https://yourapp.com/notify",
// *  }
// * @author vivi.huang
// * @since 2020/5/17
// */
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Builder(builderMethodName = "newBuilder")
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//public class CombineOrderRequest extends BasePayRequest {
//
//    private static final long serialVersionUID = -2937507949804110860L;
//
//    /**
//     * 合单商户appid
//     * 描述：合单发起方的appid。
//     * 示例值：wxd678efh567hg6787
//     * 必填
//     */
//    @JSONField(name = "combine_appid")
//    private String combineAppId;
//
//    /**
//     * 合单商户号
//     * 合单发起方商户号
//     * 示例值：1900000109
//     * 必填
//     */
//    @JSONField(name = "combine_mchid")
//    private String combineMchid;
//
//    /**
//     * 合单商户订单号
//     * 合单支付总订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
//     * 示例值：P20150806125346
//     */
//    @JSONField(name= "combine_out_trade_no")
//    private String combineOutTradeNo;
//
//    /**
//     *  场景信息
//     *  支付场景信息描述
//     *  不是必填
//     */
//    @JSONField(name = "scene_info")
//    private SceneInfo sceneInfo;
//
//    /**
//     * 最多支持子单条数：50
//     * 必填
//     */
//    @JSONField(name = "sub_orders")
//    private List<SubOrder> subOrders;
//
//    /**
//     * 支付者
//     * 必填
//     */
//    @JSONField(name = "combine_payer_info")
//    private CombinePayerInfo combinePayerInfo;
//
//    /**
//     * 交易起始时间
//     * 非必填	query订单生成时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
// *     示例值：2019-12-31T15:59:60+08:00
//     */
//    @JSONField(name = "time_start")
//    private String timeStart;
//
//    /**
//     * 交易结束时间	time_expire	string(14)
//     * 非必填	query订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
// *     示例值：2019-12-31T15:59:60+08:00
//     */
//    @JSONField(name = "time_expire")
//    private String timeExpire;
//
//    /**
//     * 通知地址
//     * 是	query接收微信支付异步通知回调地址，通知url必须为直接可访问的URL，不能携带参数。
// *   *格式: URL
// *   *示例值：https://yourapp.com/notify
//     */
//    @JSONField(name = "notify_url")
//    private String notifyUrl;
//
//
//    @Override
//    public String getUrl() {
//        return null;
//    }
//
//    @Override
//    public int getReadTimeoutMs() {
//        return 0;
//    }
//
//    @Override
//    public int getConnectTimeoutMs() {
//        return 0;
//    }
//
//    @Override
//    public String getRequestData() {
//        return null;
//    }
//}