//package com.iflytek.gulimall.order.vo;
//
//import com.alibaba.fastjson.annotation.JSONField;
//
//import com.iflytek.gulimall.order.vo.combine.CombinePayerInfo;
//import com.iflytek.gulimall.order.vo.combine.SceneInfo;
//import com.iflytek.gulimall.order.vo.combine.SubOrder;
//import lombok.*;
//import lombok.experimental.Accessors;
//
//import java.util.List;
//
///**
// * <p>
// *   合并查询结果
// * </p>
// *
// * @author vivi.huang
// * @since 2020/5/17
// */
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Builder(builderMethodName = "newBuilder")
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//public class QueryOrderResult extends BasePayResult {
//
//    private static final long serialVersionUID = 1960459269579244358L;
//
//    /**
//     * 合单商户appid
//     * 合单发起方的appid
//     * 示例值：wxd678efh567hg6787
//     */
//    @JSONField(name = "combine_appid")
//    private String combineAppId;
//
//    /**
//     * 合单商户号
//     * 合单发起方商户号
//     * 示例值：1900000109
//     */
//    @JSONField(name = "combine_mchid")
//    private String combineMchId;
//
//    /**
//     * 合单商户订单号
//     * 合单支付总订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
//     */
//    @JSONField(name = "combine_out_trade_no")
//    private String combineOutTradeNo;
//
//    /**
//     * 场景信息
//     * 支付场景信息描述
//     */
//    @JSONField(name = "scene_info")
//    private SceneInfo sceneInfo;
//
//    /**
//     * 子单信息
//     * 最多支持子单条数：50
//     */
//    @JSONField(name = "sub_orders")
//    private List<SubOrder> subOrders;
//
//    /**
//     * 支付者
//     * 示例值：见请求示例
//     */
//    @JSONField(name = "combine_payer_info")
//    private CombinePayerInfo combinePayerInfo;
//
//
//    public QueryOrderResult(String content) {
//    }
//}