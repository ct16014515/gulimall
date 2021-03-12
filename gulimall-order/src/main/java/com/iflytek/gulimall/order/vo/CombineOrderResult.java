package com.iflytek.gulimall.order.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   合并下单结果
 * </p>
 *
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
public class CombineOrderResult extends BasePayResult {

    private static final long serialVersionUID = 1960459269579244358L;


    /**
     * 预支付交易会话标识
     * 数字和字母。微信生成的预支付会话标识，用于后续接口调用使用。
     * 示例值：wx201410272009395522657a690389285100
     */
    @JSONField(name = "prepay_id")
    private String prepayId;


    public CombineOrderResult(String content) {
    }
}