package com.gulimall.testredislistener.redis.listener;

import com.gulimall.testredislistener.service.CereBuyerCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;


@Component
public class RedisListener extends KeyExpirationEventMessageListener {

    private RedisTemplate<String, Object> redisTemplate;



    private CereBuyerCouponService cereBuyerCouponService;



    public RedisListener(RedisMessageListenerContainer listenerContainer,
                         RedisTemplate redisTemplate,
                         CereBuyerCouponService cereBuyerCouponService) {
        super(listenerContainer);
        this.redisTemplate=redisTemplate;
        this.cereBuyerCouponService=cereBuyerCouponService;

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        redisTemplate.delete(expiredKey);
    }


}
