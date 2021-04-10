package com.gulimall.testredislistener.redis.config;

import com.gulimall.testredislistener.redis.listener.RedisListener;
import com.gulimall.testredislistener.service.CereBuyerCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisMessageListener {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;



	@Autowired
	private CereBuyerCouponService cereBuyerCouponService;



	@Bean
	public RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory redisConnectionFactory){
		RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
		listenerContainer.setConnectionFactory(redisConnectionFactory);
		// 若是监听所有DB，则注释 下面代码
		RedisListener redisListener = new RedisListener(listenerContainer,redisTemplate,
				cereBuyerCouponService
				);
		listenerContainer.addMessageListener(redisListener, new PatternTopic("__keyevent@0__:expired"));
		return listenerContainer;
	}
}
