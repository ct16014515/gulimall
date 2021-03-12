package com.iflytek.gulimall.order.vo.combine;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *     支付通知回调通知数据
 * </p>
 * {
 *  "combine_appid": "wxd678efh567hg6787",
 * "combine_out_trade_no": "20150806125346",
 *  "combine_mchid": "1900000109",
 *  "scene_info": {
 *    "device_id": "POS1:1"
 *  },
 *  "sub_orders": [
 *    {
 *      "mchid": "1900000109",
 *      "trade_type": "JSAPI",
 *      "trade_state": "SUCCESS",
 *      "bank_type": "CMC",
 *      "attach": "深圳分店",
 *      "amount": {
 *        "total_amount": 10,
 *        "currency": "CNY",
 *        "payer_amount": 10,
 *        "payer_currency": "CNY"
 *      },
 *      "success_time": "2015-05-20T13:29:35.120+08:00",
 *      "transaction_id": "1009660380201506130728806387",
 *      "out_trade_no": "20150806125346",
 *      "sub_mchid": "1900000109"
 *    }
 *  ],
 *  "combine_payer_info": {
 *    "openid": "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
 *  }
 * }
 * 支付成功通知参数
 *
 * @author vivi.huang
 * @since 2020/5/21
 */
@Data
@EqualsAndHashCode()
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Resource implements Serializable {


    private static final long serialVersionUID = 1078609106413418449L;

    /**
     * 加密算法类型	algorithm	string(32)	是	对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
     *     示例值：AEAD_AES_256_GCM
     */
    @JSONField(name = "algorithm")
    private String algorithm;

    /**
     * 数据密文	ciphertext	string(1048576)	是	Base64编码后的开启/停用结果数据密文
     *     示例值：sadsadsadsad
     */
    @JSONField(name = "ciphertext")
    private String ciphertext;

    /**
     * 附加数据	associated_data	string(16)	否	附加数据
     *     示例值：fdasfwqewlkja484w
     */
    @JSONField(name = "associated_data")
    private String associatedData;

    /**
     *  随机串	nonce	string(16)	是	加密使用的随机串
     *     示例值：fdasflkja484w
     */
    @JSONField(name = "nonce")
    private String nonce;

    /**
     * 加密前的对象类型	original_type	string(32)	是	加密前的对象类型，退款通知的类型为refund
     * 示例值：refund
     */
    @JSONField(name = "original_type")
    private String originalType;




}