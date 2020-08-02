package com.iflytek.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}

