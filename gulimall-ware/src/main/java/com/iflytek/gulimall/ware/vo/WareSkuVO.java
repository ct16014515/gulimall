package com.iflytek.gulimall.ware.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WareSkuVO implements Serializable {

    private Long id; //wms_ware_sku的主键
    private Integer skuRemainingQuantity; //仓库剩余的数量 为仓库的总数量-已经锁定的数量-sku的数量(需要锁定的数量)
    private Long wareId;//仓库id

}
