package com.iflytek.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.iflytek.common.model.vo.order.OrderEntityVO;
import com.iflytek.common.model.vo.product.WareHasStockVO;
import com.iflytek.common.model.vo.product.WareSkuLockVO;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.iflytek.gulimall.ware.vo.WareStockDelayVO;


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

    ResultBody<Integer> hasStock(Long skuId);

    ResultBody wareSkuLock(WareSkuLockVO wareSkuLockVO);

    void stockRelease(WareStockDelayVO wareStockDelayVO);

    void stockRelease(OrderEntityVO orderEntityVO);
}

