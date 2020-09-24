package com.iflytek.gulimall.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderSubmitVO implements Serializable {

    private Long addressId;
    private Integer payType;
    private BigDecimal payMoney;
    private String orderToken;

}
