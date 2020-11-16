package com.iflytek.gulimall.common.model.mq.to;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class SecKillOrderCreateTO {

    private String orderSn;
    private Long skuId;
    private Integer number;
    private BigDecimal price;
    private Long promotionSessionId;
    private Long memberId;


}
