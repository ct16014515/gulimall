package com.iflytek.common.model.vo;

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
    private Integer hasStock;

    public WareHasStockVO() {
    }

    public WareHasStockVO(Long skuId, Integer hasStock) {
        this.skuId = skuId;
        this.hasStock = hasStock;
    }
}
