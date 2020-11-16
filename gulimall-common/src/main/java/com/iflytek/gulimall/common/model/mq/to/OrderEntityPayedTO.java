package com.iflytek.gulimall.common.model.mq.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付后发送的实体
 */
@Data
public class OrderEntityPayedTO {


    private Long id;

    private String orderSn;

    /**
     * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
     */
    private Integer status;

}
