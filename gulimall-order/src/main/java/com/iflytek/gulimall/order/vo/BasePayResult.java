package com.iflytek.gulimall.order.vo;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 *   微信支付结果基础实体
 *
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@Accessors(chain = true)
public abstract class BasePayResult extends BaseEntity {

    private static final long serialVersionUID = -7655085202024001080L;




}