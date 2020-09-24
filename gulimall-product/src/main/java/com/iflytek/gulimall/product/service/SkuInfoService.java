package com.iflytek.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iflytek.common.model.vo.order.OrderItemVO;
import com.iflytek.common.model.vo.product.SkuInfoPriceVO;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.product.entity.SkuInfoEntity;
import com.iflytek.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    SkuItemVo getSkuItem(Long skuId) throws ExecutionException, InterruptedException;
    SkuItemVo getSkuItemNoFuture(Long skuId) ;

    ResultBody<List<String>> getskuAttrsBySkuId(Long skuId);

    ResultBody<List<SkuInfoPriceVO>> getSkuPriceBySkuIds(List<Long> skuIds);

    List<OrderItemVO> getOrderItemsBySkuIds(List<Long> skuIds);
}

