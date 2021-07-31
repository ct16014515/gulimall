package com.iflytek.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.exception.RRException;
import com.iflytek.gulimall.common.feign.CouponServiceAPI;
import com.iflytek.gulimall.common.feign.SerachServiceAPI;
import com.iflytek.gulimall.common.feign.WareServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SkuEsModel;
import com.iflytek.gulimall.common.feign.vo.SkuReductionVO;
import com.iflytek.gulimall.common.feign.vo.WareHasStockVO;
import com.iflytek.gulimall.common.model.es.Attr;


import com.iflytek.gulimall.common.feign.vo.SpuBoundVO;
import com.iflytek.gulimall.common.utils.ResultBody;

import com.iflytek.gulimall.product.dao.*;
import com.iflytek.gulimall.product.entity.*;

import com.iflytek.gulimall.product.service.*;
import com.iflytek.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    WareServiceAPI wareServiceAPI;
    @Autowired
    ProductAttrValueDao productAttrValueDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    SerachServiceAPI serachServiceAPI;


    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;


    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    CouponServiceAPI couponServiceAPI;


    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;


    @Autowired
    private SkuImagesService skuImagesService;


    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("id", key).or().like("spu_name", key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);


        return new PageUtils(page);
    }

    /**
     * 商品上架
     *
     * @param spuId 1、将属性改为已上架，
     *              2、构建SkuEsModel模型,再远程调用搜索服务,向搜索服务索引记录
     */
    @Override
    public void up(Long spuId) {

        List<SkuInfoEntity> skuList = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<Long> skuIdList = skuList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //TODO 1、hasStock是否有库存 远程调用库存服务查询是否有库存
        Map<Long, Integer> stockVosMap = null;

        TypeReference<List<WareHasStockVO>> typeReference = new TypeReference<List<WareHasStockVO>>() {
        };
        List<WareHasStockVO> stockVos = wareServiceAPI.hasStock(skuIdList).getData(typeReference)
                .stream().filter(item -> item.getHasStock() == 1).collect(Collectors.toList());
        if (stockVos.size() == 0) {
            throw new RRException(GulimallExceptinCodeEnum.WARE_SETTING_ERROR);
        }
        stockVosMap = stockVos.stream().collect(Collectors.toMap(WareHasStockVO::getSkuId, WareHasStockVO::getHasStock));

        //TODO 4、查询当前sku用来被检索的属性规格
        List<Attr> attrs = productAttrValueDao.selectAttrsBySpuId(spuId);
        List<Attr> attrList = new ArrayList<>();
        attrs.forEach(a -> {
            String valueSelect = a.getValueSelect();
            List<String> strings = JSON.parseObject(valueSelect, new TypeReference<List<String>>() {
            });
            strings.forEach(s -> {
                Attr attr1 = new Attr();
                attr1.setAttrValue(s);
                attr1.setAttrName(a.getAttrName());
                attr1.setAttrId(a.getAttrId());
                attrList.add(attr1);
            });
        });
        Map<Long, Integer> finalStockVosMap = stockVosMap;
        List<SkuEsModel> esModels = skuList.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());//价格
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());//图片
            //是否有库存,如果远程调用失败默认为有库存
            skuEsModel.setHasStock(finalStockVosMap == null ? 0 : finalStockVosMap.get(skuInfoEntity.getSkuId()));//是否头库存
            //TODO 2、 hotScore 热度评分 0
            skuEsModel.setHotScore(0L);
            //TODO 3、查询品牌和分类信息
            //品牌名称 分类名称
            BrandEntity brandEntity = brandDao.selectById(skuInfoEntity.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            String categoryName = categoryDao.selectNameById(skuInfoEntity.getCatalogId());
            skuEsModel.setCatalogName(categoryName);
            //当前sku用来被检索的属性
            skuEsModel.setAttrs(attrList);
            return skuEsModel;
        }).collect(Collectors.toList());
        ResultBody resultBody = serachServiceAPI.productUp(esModels);
        if (0 == resultBody.getCode()) {
            SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
            spuInfoEntity.setId(spuId);
            spuInfoEntity.setPublishStatus(1);
            spuInfoEntity.setUpdateTime(new Date());
            this.updateById(spuInfoEntity);
        } else {
            throw new RRException(GulimallExceptinCodeEnum.PRODUCT_UP_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savesupInfo(SpuSaveVo vo) {
        //1、保存spu基本信息：pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //2、保存spu的描述图片：pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //3、保存spu的图片集：pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        //4、保存spu的规格参数：pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            //查询attr属性名
            AttrEntity byId = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(byId.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);


        //5、保存spu的积分信息：gulimall_sms--->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundVO spuBoundVO = new SpuBoundVO();
        BeanUtils.copyProperties(bounds, spuBoundVO);
        spuBoundVO.setSpuId(spuInfoEntity.getId());
        couponServiceAPI.saveSpuBounds(spuBoundVO);


        //5、保存当前spu对应的所有sku信息：pms_sku_info
        //5、1）、sku的基本信息:pms_sku_info
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setWeight(vo.getWeight());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());

                //5、2）、sku的图片信息：pms_sku_images
                skuImagesService.saveBatch(imagesEntities);

                //5、3）、sku的销售属性：pms_sku_sale_attr_value
                List<com.iflytek.gulimall.product.vo.Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //5、4）、sku的优惠、满减等信息：gulimall_sms--->sms_sku_ladder、sms_sku_full_reduction、sms_member_price
                SkuReductionVO skuReductionVO = new SkuReductionVO();
                BeanUtils.copyProperties(item, skuReductionVO);
                skuReductionVO.setSkuId(skuId);
                if (skuReductionVO.getFullCount() > 0 || skuReductionVO.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    couponServiceAPI.saveSkuReduction(skuReductionVO);
                }

            });
        }
    }


    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {

        this.baseMapper.insert(spuInfoEntity);

    }

    public static void main(String[] args) {
        String str = "[\"3000mAh\"]";
        List<String> strings = JSON.parseObject(str, new TypeReference<List<String>>() {
        });
        System.out.println(strings);
    }
}