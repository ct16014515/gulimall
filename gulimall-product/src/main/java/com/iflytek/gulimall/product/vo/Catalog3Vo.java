package com.iflytek.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 三级分类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Catalog3Vo {
    private String catalog2Id;//2级分类id即父id
    private String id;//2级分类id
    private String name;//分类名称
}
