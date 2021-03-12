//package com.iflytek.gulimall.order.vo.combine;
//
//import com.alibaba.fastjson.annotation.JSONField;
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import java.io.Serializable;
//
///**
// * <p>
// *      最多支持子单条数：50
// * </p>
// *
// * @author vivi.huang
// * @since 2020/5/17
// */
//@Data
//@EqualsAndHashCode()
//@Builder(builderMethodName = "newBuilder")
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//public class SubOrder implements Serializable {
//
//    private static final long serialVersionUID = -8574234833013449051L;
//
//    /**
//     * 子单商户号
//     * 子单发起方商户号，必须与发起方appid有绑定关系
//     * 示例值：1900000109
//     */
//    @JSONField(name = "mchid")
//    private String mchId;
//
//    /**
//     * 附加数据
//     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
//     * 示例值：深圳分店
//     */
//    @JSONField(name = "attach")
//    private String attach;
//
//    /**
//     * 订单金额
//     * 必填
//     */
//    @JSONField(name = "amount")
//    private Amount amount;
//
//    /**
//     * 子单商户订单号
//     * 必填
//     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
//     *     特殊规则：最小字符长度为6
//     *     示例值：20150806125346
//     */
//    @JSONField(name = "out_trade_no")
//    private String outTradeNo;
//
//    /**
//     * 二级商户号
//     * 必填
//     * 二级商户商户号，由微信支付生成并下发。
//     *     注意：仅适用于电商平台 服务商
//     *     示例值：1900000109
//     */
//    @JSONField(name = "sub_mchid")
//    private String subMchId;
//
//    /**
//     * 商品详情
//     * 不是必填
//     * 商品详细描述（商品列表)
//     */
//    @JSONField(name = "detail")
//    private String detail;
//
//    /**
//     * 商品描述
//     * 必填
//     * 商品简单描述。需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
//     *  示例值：腾讯充值中心-QQ会员充值
//     */
//    @JSONField(name = "description")
//    private String description;
//
//    /**
//     * 结算信息
//     * 不是必填
//     */
//    @JSONField(name = "settle_info")
//    private SettleInfo settleInfo;
//}