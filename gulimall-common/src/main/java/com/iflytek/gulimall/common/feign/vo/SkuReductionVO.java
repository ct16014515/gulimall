package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class SkuReductionVO {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
