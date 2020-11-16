package com.iflytek.gulimall.common.model.mq.to;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WareStockDelayTO implements Serializable {


    private String orderSn;

    private List<Long> wareOrderTaskDetailIds; //wms_ware_order_task_detail表主键集合

}
