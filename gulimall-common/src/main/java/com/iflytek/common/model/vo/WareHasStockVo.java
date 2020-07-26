package com.iflytek.common.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 是否有skuid是否有库存
 */
@Data
@ToString
public class WareHasStockVo implements Serializable {

    private Long skuId;
    private Boolean hasStock;

    public WareHasStockVo() {
    }

    public WareHasStockVo(Long skuId, Boolean hasStock) {
        this.skuId = skuId;
        this.hasStock = hasStock;
    }
}
