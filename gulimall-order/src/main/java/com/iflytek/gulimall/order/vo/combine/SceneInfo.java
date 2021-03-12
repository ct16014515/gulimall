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
// *      场景信息
// *      支付场景信息描述
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
//public class SceneInfo implements Serializable {
//
//    private static final long serialVersionUID = 4989044089223014924L;
//
//    /**
//     * 终端设备号（门店号或收银设备ID）
//     * 特殊规则：长度最小7个字节
//     * 示例值：POS1:1
//     */
//    @JSONField(name = "device_id")
//    private String deviceId;
//
//    /**
//     * 用户终端IP
//     * 格式: ip(ipv4+ipv6)
//     * 示例值：14.17.22.32
//     */
//    @JSONField(name = "payer_client_ip")
//    private String payerClientIp;
//}