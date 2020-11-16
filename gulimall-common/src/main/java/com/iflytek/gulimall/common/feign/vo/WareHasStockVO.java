package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 是否有skuid是否有库存
 */
@Data
@ToString
public class WareHasStockVO implements Serializable {

    private Long skuId;
    private Integer hasStock; // 0没库存 1有库存
}
