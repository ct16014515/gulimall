package com.iflytek.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.product.entity.SkuImagesEntity;
import com.iflytek.gulimall.product.entity.SpuInfoDescEntity;
import com.iflytek.gulimall.product.feign.SearchFeignService;
import com.iflytek.gulimall.product.service.*;
import com.iflytek.gulimall.product.vo.SeckillSkuVo;
import com.iflytek.gulimall.product.vo.SkuItemSaleAttrVo;
import com.iflytek.gulimall.product.vo.SkuItemVo;
import com.iflytek.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.Query;

import com.iflytek.gulimall.product.dao.SkuInfoDao;
import com.iflytek.gulimall.product.entity.SkuInfoEntity;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {


    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private TaskExecutor executor;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SearchFeignService seckillFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 商品详情页数据使用异步线程获取
     * @param skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public SkuItemVo getSkuItem(Long skuId) throws ExecutionException, InterruptedException {
        long begin = System.currentTimeMillis();
        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1、sku基本信息的获取  pms_sku_info
            SkuInfoEntity info = this.getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);


        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //3、获取spu的销售属性组合
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, executor);


        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //4、获取spu的介绍    pms_spu_info_desc
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, executor);


        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //5、获取spu的规格参数信息
            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVos);
        }, executor);


        // Long spuId = info.getSpuId();
        // Long catalogId = info.getCatalogId();

        //2、sku的图片信息    pms_sku_images
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> imagesEntities = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(imagesEntities);
        }, executor);

        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            //3、远程调用查询当前sku是否参与秒杀优惠活动
            ResultBody<SeckillSkuVo> skuSeckilInfo = seckillFeignService.getSkuSeckilInfo(skuId);
            if (skuSeckilInfo.getCode() == 0) {
                //查询成功
                SeckillSkuVo seckillSkuVo = skuSeckilInfo.getData();
                skuItemVo.setSeckillSkuVo(seckillSkuVo);
                if (seckillSkuVo != null) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > seckillSkuVo.getEndTime()) {
                        skuItemVo.setSeckillSkuVo(null);
                    }
                }
            }
        }, executor);
        //等到所有任务都完成
        CompletableFuture.allOf(saleAttrFuture, descFuture, baseAttrFuture, imageFuture,seckillFuture).get();
        System.out.println("耗时:"+(System.currentTimeMillis()-begin)+"毫秒");
        return skuItemVo;
    }

    @Override
    public SkuItemVo getSkuItemNoFuture(Long skuId)  {
        long begin = System.currentTimeMillis();
        SkuItemVo skuItemVo = new SkuItemVo();

        //1、sku基本信息的获取  pms_sku_info
        SkuInfoEntity info = this.getById(skuId);
        skuItemVo.setInfo(info);

        //2、获取spu的销售属性组合
        Long spuId = info.getSpuId();
        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(spuId);
        skuItemVo.setSaleAttr(saleAttrVos);
        //3、获取spu的介绍    pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
        skuItemVo.setDesc(spuInfoDescEntity);
        //4、获取spu的规格参数信息
        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, info.getCatalogId());
        skuItemVo.setGroupAttrs(attrGroupVos);
        //5图片信息
        List<SkuImagesEntity> imagesEntities = skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(imagesEntities);
        System.out.println("耗时:"+(System.currentTimeMillis()-begin)+"毫秒");
        return skuItemVo;
    }


}