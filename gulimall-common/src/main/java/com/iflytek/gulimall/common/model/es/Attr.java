package com.iflytek.gulimall.common.model.es;

import lombok.Data;

@Data
public class Attr {
    private Long attrId;//属性id

    private String attrName;//属性名称

    private String attrValue;//属性值

    private String valueSelect;//属性值,["3000mAh","4000mAh","5000mAh"] list集合


}
