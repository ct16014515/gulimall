package com.iflytek.common.model.vo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WareSkuLockVO implements Serializable {

    private String orderSn;

    private List<SkuOrderItem> skuOrderItems;

    private Long orderId;

    private String receiverName;//收获人姓名

    private String receiverPhone;//收获人电话

    private String receiverAddress;//收货人地址

    private String note;//订单备注


}
