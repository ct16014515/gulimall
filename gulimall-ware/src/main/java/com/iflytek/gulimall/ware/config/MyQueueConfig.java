package com.iflytek.gulimall.ware.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.iflytek.gulimall.common.constant.MqConstant.*;
import static com.iflytek.gulimall.common.constant.WareConstant.*;

/**
 * 场景1、成功下订单后，锁定库存，当订单1分钟没有支付时，需要使用延时队列来释放库存。
 * 场景2、锁定库存后,订单业务异常,需要释放库存.
 *
 *
 */
@Configuration
public class MyQueueConfig {


    /**
     * 库存锁定延时队列
     *
     * @return
     */
    @Bean
    public Queue wareDelayQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> argument
        Map<String, Object> argument = new HashMap<>();
        argument.put("x-dead-letter-exchange", MQ_WARE_EXCHANGE);
        argument.put("x-dead-letter-routing-key", MQ_STOCK_RELEASE_ROUTINGKEY);
        argument.put("x-message-ttl", 3 * 60 * 1000);
        Queue queue = new Queue(MQ_STOCK_DELAY_QUEUE,
                true,
                false,
                false,
                argument
        );
        return queue;
    }

    /**
     * 库存解锁队列
     *
     * @return
     */
    @Bean
    public Queue wareReleaseStockQueue() {
        Queue queue = new Queue(MQ_STOCK_RELEASE_QUEUE,
                true,
                false,
                false,
                null
        );
        return queue;

    }

    @Bean
    public Exchange wareEventExchange() {
        TopicExchange topicExchange = new TopicExchange(
                MQ_WARE_EXCHANGE, true, false);
        return topicExchange;
    }

    /**
     * 库存释放绑定
     *
     * @return
     */
    @Bean
    public Binding wareStockReleaseBinding() {
        return new Binding(
                MQ_STOCK_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_WARE_EXCHANGE,
                MQ_STOCK_RELEASE_ROUTINGKEY,
                null);
    }

    /**
     * 库存锁定绑定
     *
     * @return
     */
    @Bean
    public Binding wareStockLockedBinding() {

        return new Binding(
                MQ_STOCK_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_WARE_EXCHANGE,
                MQ_STOCK_LOCKED_ROUTINGKEY,
                null);
    }


}
