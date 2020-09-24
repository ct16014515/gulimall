package com.iflytek.common.model.vo.product;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@ToString
public class SkuInfoPriceVO implements Serializable {
    private Long skuId;
    private BigDecimal price; // 0没库存 1有库存
}
