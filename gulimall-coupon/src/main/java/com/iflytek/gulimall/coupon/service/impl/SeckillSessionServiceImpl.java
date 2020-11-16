package com.iflytek.gulimall.coupon.service.impl;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.gulimall.common.constant.CouponConstant;
import com.iflytek.gulimall.common.feign.ProductServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SecSessionSkuVO;
import com.iflytek.gulimall.common.feign.vo.SkuInfoVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.coupon.dao.SeckillSkuRelationDao;
import com.iflytek.gulimall.coupon.entity.SeckillSkuRelationEntity;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;

import com.iflytek.gulimall.coupon.dao.SeckillSessionDao;
import com.iflytek.gulimall.coupon.entity.SeckillSessionEntity;
import com.iflytek.gulimall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {


    @Autowired
    SeckillSkuRelationDao seckillSkuRelationDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    ProductServiceAPI productServiceAPI;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void uploadSeckillProductLast3Days() {
        //查询最近三天的活动信息
        List<SeckillSessionEntity> seckillSessionEntities = baseMapper.selectSeckillSessionLast3Days();
        seckillSessionEntities.forEach(item -> {
            Long sessionId = item.getId();
            List<SeckillSkuRelationEntity> skuRelationEntityList = seckillSkuRelationDao.selectList(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", sessionId));
            item.setSkuRelationEntityList(skuRelationEntityList);
        });
        saveSessionInfos(seckillSessionEntities);
        saveSessionSkus(seckillSessionEntities);
    }

    /**
     * 缓存商品信息 {"5_2":{}} 这种数据格式,
     * 假如第一次定时任务和第二次定时任务之间商户上架秒杀商品,可以添加到redis
     * 切记只用异步线程会导致并发问题
     */

    @Async
    public void saveSessionSkus(List<SeckillSessionEntity> seckillSessionEntities) {
        seckillSessionEntities.forEach(item -> {
            long startTime = item.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long endTime = item.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long ttl = endTime - new Date().getTime();
            List<SeckillSkuRelationEntity> relationEntityList = item.getSkuRelationEntityList();
            List<Long> skuIds = relationEntityList.stream().map(SeckillSkuRelationEntity::getSkuId).collect(Collectors.toList());
            ResultBody<List<SkuInfoVO>> resultBody = productServiceAPI.list(skuIds);
            if (resultBody.getData() != null && resultBody.getData().size() > 0) {
                Map<Long, SkuInfoVO> skuInfoVOMap = resultBody.getData().stream().collect(Collectors.toMap(SkuInfoVO::getSkuId, skuInfoVO -> skuInfoVO));
                relationEntityList.forEach(seckillSkuRelationEntity -> {
                            String skuKey = CouponConstant.SECKILL_SESSIONSKU_CACHE_PREFIX + seckillSkuRelationEntity.getPromotionSessionId() + "_" + seckillSkuRelationEntity.getSkuId();
                            if (!redisTemplate.hasKey(skuKey)) {
                                SecSessionSkuVO secSessionSkuVO = new SecSessionSkuVO();
                                //秒杀信息
                                BeanUtils.copyProperties(seckillSkuRelationEntity, secSessionSkuVO);
                                //商品详细信息
                                SkuInfoVO skuInfoVO = skuInfoVOMap.get(seckillSkuRelationEntity.getSkuId());
                                if (skuInfoVO != null) {
                                    secSessionSkuVO.setSkuInfo(skuInfoVO);
                                }
                                //秒杀开始时间
                                secSessionSkuVO.setStartTime(startTime);
                                //秒杀结束时间
                                secSessionSkuVO.setEndTime(endTime);
                                //商品随机码
                                String token = UUID.randomUUID().toString().replace("-", "");
                                secSessionSkuVO.setRandomCode(token);
                                String toJSONString = JSON.toJSONString(secSessionSkuVO);
                                redisTemplate.opsForValue().set(skuKey, toJSONString,ttl,TimeUnit.MILLISECONDS);
                                //设置商品的库存信息,使用redisson信号量,限流作用
                                RSemaphore semaphore = redissonClient.getSemaphore(CouponConstant.SKU_STOCK_SEMAPHORE + token);
                                semaphore.trySetPermitsAsync(seckillSkuRelationEntity.getSeckillCount());
                            }
                        }
                );
            }
        });
    }

    /**
     * 缓存秒杀活动
     *
     * @param seckillSessionEntities
     */
    @Async
    public void saveSessionInfos(List<SeckillSessionEntity> seckillSessionEntities) {
        seckillSessionEntities.forEach(item -> {
            long startTime = item.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long endTime = item.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long ttl = endTime - new Date().getTime();
            String key = CouponConstant.SECKILL_SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            if (!redisTemplate.hasKey(key)) {
                List<String> collect = item.getSkuRelationEntityList().stream().map(seckillSkuRelationEntity ->
                        seckillSkuRelationEntity.getPromotionSessionId() + "_" + seckillSkuRelationEntity.getSkuId()
                ).collect(Collectors.toList());
                String jsonString = JSON.toJSONString(collect);
                redisTemplate.opsForValue().set(key, jsonString, ttl, TimeUnit.MILLISECONDS);
            }
        });
    }


}