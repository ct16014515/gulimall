package com.iflytek.gulimall.seckill.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.iflytek.gulimall.common.constant.CouponConstant;
import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.feign.vo.MemberVO;
import com.iflytek.gulimall.common.feign.vo.SecSessionSkuVO;
import com.iflytek.gulimall.common.feign.vo.SendMessageRequest;
import com.iflytek.gulimall.common.model.mq.to.SecKillOrderCreateTO;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.iflytek.gulimall.common.constant.CouponConstant.*;
import static com.iflytek.gulimall.common.constant.MqConstant.MQ_ORDER_EXCHANGE;
import static com.iflytek.gulimall.common.constant.MqConstant.MQ_SECKILL_ORDER_CREATE_ROUTINGKEY;
import static com.iflytek.gulimall.seckill.interceptor.LoginInterceptor.toThreadLocal;

@Service
public class SecKillService {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    MqServiceAPI mqServiceAPI;

    public List<SecSessionSkuVO> getCurrentSecSessionSkuVOS() {
        long currentTime = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SECKILL_SESSION_CACHE_PREFIX + "*");
        //"seckill:session:1604912400000_1604916000000"
        List<SecSessionSkuVO> secSessionSkuVOS = new ArrayList<>();
        if (keys != null) {
            keys.forEach(key -> {
                String[] strings = key.replace(SECKILL_SESSION_CACHE_PREFIX, "").split("_");
                long startTime = Long.parseLong(strings[0]);
                long endTime = Long.parseLong(strings[1]);
                if (currentTime >= startTime && currentTime <= endTime) {
                    //????????????????????????????????????,????????????????????????,?????????????????????,?????????????????????
                    String sessionIdsStr = redisTemplate.opsForValue().get(key);
                    List<String> skuIds = JSON.parseArray(sessionIdsStr, String.class);
                    if (skuIds != null) {
                        skuIds.forEach(skuId -> {
                            String secSessionSkuVOStr = redisTemplate.opsForValue().get(SECKILL_SESSIONSKU_CACHE_PREFIX + skuId);
                            SecSessionSkuVO secSessionSkuVO = JSON.parseObject(secSessionSkuVOStr, SecSessionSkuVO.class);
                            secSessionSkuVOS.add(secSessionSkuVO);
                        });
                    }
                }
            });
        }
        return secSessionSkuVOS;
    }

    public SecSessionSkuVO getSecSessionSkuVOBySkuId(Long skuId) {
        //seckill:sku:*_skuId
        Set<String> keys = redisTemplate.keys(SECKILL_SESSIONSKU_CACHE_PREFIX + "*_" + skuId);
        if (keys != null && keys.size() == 1) {
            String key = keys.stream().findFirst().get();
            String secSessionSkuVOStr = redisTemplate.opsForValue().get(key);
            SecSessionSkuVO secSessionSkuVO = JSON.parseObject(secSessionSkuVOStr, SecSessionSkuVO.class);
            //????????????????????????????????????????????????????????????????????????,???????????????????????????,??????????????????
            if (secSessionSkuVO != null) {
                long currentTime = new Date().getTime();
                Long startTime = secSessionSkuVO.getStartTime();
                Long endTime = secSessionSkuVO.getEndTime();
                if (currentTime < startTime) {
                    secSessionSkuVO.setRandomCode("");
                }
                if (currentTime > endTime) {
                    return null;
                }
            }
            return secSessionSkuVO;
        }
        return null;
    }

    public String kill(String key, String randomCode, Integer number) {
        long start = System.currentTimeMillis();
        MemberVO memberVO = toThreadLocal.get();
        String skuJson = redisTemplate.opsForValue().get(SECKILL_SESSIONSKU_CACHE_PREFIX + key);
        if (!StringUtils.isEmpty(skuJson)) {
            SecSessionSkuVO secSessionSkuVO = JSON.parseObject(skuJson, SecSessionSkuVO.class);
            long current = new Date().getTime();
            Long startTime = secSessionSkuVO.getStartTime();
            Long endTime = secSessionSkuVO.getEndTime();
            long ttl = endTime - current;//????????????,
            //?????????,?????????,??????????????????
            if (current >= startTime && current <= endTime && randomCode.equals(secSessionSkuVO.getRandomCode()) && number <= secSessionSkuVO.getSeckillLimit()) {
                //???key????????????
                String verifyKey = secSessionSkuVO.getPromotionSessionId() + "_" + secSessionSkuVO.getSkuId();
                if (verifyKey.equals(key)) {
                    //?????????????????????????????????????????????????????????,??????????????????,?????????????????????????????????????????????userId-key
                    Boolean isBuy = redisTemplate.opsForValue().setIfAbsent(memberVO.getUserId() + "_" + key, number.toString(), ttl, TimeUnit.MILLISECONDS);
                    if (isBuy != null && isBuy) {
                        //????????????,???????????????????????????
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                        /**
                         ** @param permits amount of permits ??????
                         * * @param waitTime the maximum time to wait ????????????????????????
                         ** @param unit the time unit,??????
                         */
                        boolean isKillSuccess = semaphore.tryAcquire(number);
                        if (isKillSuccess) {
                            String orderSn = IdWorker.getTimeId();
                            //????????????????????????????????????????????????
                            SecKillOrderCreateTO secKillOrderCreateTO = new SecKillOrderCreateTO();
                            secKillOrderCreateTO.setMemberId(memberVO.getUserId());
                            secKillOrderCreateTO.setNumber(number);
                            secKillOrderCreateTO.setOrderSn(orderSn);
                            secKillOrderCreateTO.setPrice(secSessionSkuVO.getSeckillPrice());
                            secKillOrderCreateTO.setSkuId(secSessionSkuVO.getSkuId());
                            //??????????????????????????????
                            SendMessageRequest request = SendMessageRequest.builder().routingKey(MQ_SECKILL_ORDER_CREATE_ROUTINGKEY).
                                    object(secKillOrderCreateTO).exchange(MQ_ORDER_EXCHANGE).className(secKillOrderCreateTO.getClass()
                                    .getName()).build();
                            mqServiceAPI.sendMessage(request);
                            long end = System.currentTimeMillis();
                            System.out.println("????????????:"+(end-start)+"??????");
                            return orderSn;
                        }
                    }
                }


            }

        }
        return null;
    }
}