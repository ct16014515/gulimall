package com.iflytek.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.iflytek.common.model.vo.WareHasStockVO;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.gulimall.ware.entity.WareSkuEntity;



import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:15:53
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WareHasStockVO> hasStock(List<Long> skuIds);
}

