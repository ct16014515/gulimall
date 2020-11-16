package com.iflytek.gulimall.product.vo;

import com.iflytek.gulimall.common.model.es.Attr;
import lombok.Data;
import lombok.ToString;

import java.util.List;



@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;

}
