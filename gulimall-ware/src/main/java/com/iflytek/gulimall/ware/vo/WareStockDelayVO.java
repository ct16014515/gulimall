package com.iflytek.gulimall.ware.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WareStockDelayVO implements Serializable {


    private String orderSn;

    private List<Long> wareOrderTaskDetailIds; //wms_ware_order_task_detail表主键集合

}
