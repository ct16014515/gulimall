package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class SpuBoundVO {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;

}
