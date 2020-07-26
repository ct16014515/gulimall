package com.iflytek.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 二级分类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Catalog2Vo {
    private String catalog1Id;//1级分类id即父id
    private String id;//2级分类id
    private String name;//分类名称
    private List<Catalog3Vo> catalog3List;//三级分类list
}
