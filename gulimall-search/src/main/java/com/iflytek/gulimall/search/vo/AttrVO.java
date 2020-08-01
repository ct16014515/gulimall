package com.iflytek.gulimall.search.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AttrVO {

    private Long attrId;
    private String attrName;
    private List<String> attrValue;

}
