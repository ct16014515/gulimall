package com.iflytek.gulimall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.iflytek.gulimall.common.constant.MqConstant.*;
import static com.iflytek.gulimall.common.constant.OrderConstant.*;

@Configuration
public class MyQueueConfig {


    //订单超时时间
    @Value("${order.timeOut}")
    public Integer timeOut;


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete,
        //			@Nullable Map<String, Object> argument
        Map<String, Object> argument = new HashMap<>();
        argument.put("x-dead-letter-exchange", MQ_ORDER_EXCHANGE);
        argument.put("x-dead-letter-routing-key", MQ_ORDER_RELEASE_ROUTINGKEY);
        argument.put("x-message-ttl", timeOut * 60 * 1000);
        Queue queue = new Queue(MQ_ORDER_DELAY_QUEUE,
                true,
                false,
                false,
                argument
        );
        return queue;
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        Queue queue = new Queue(MQ_ORDER_RELEASE_QUEUE,
                true,
                false,
                false,
                null
        );
        return queue;

    }

    @Bean
    public Exchange orderEventExchange() {
        TopicExchange topicExchange = new TopicExchange(
                MQ_ORDER_EXCHANGE, true, false);
        return topicExchange;
    }

    /**
     * 订单交换机绑定订单延时队列
     *
     * @return
     */
    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding(
                MQ_ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_ORDER_EXCHANGE,
                MQ_ORDER_CREATE_ROUTINGKEY,
                null);
    }

    /**
     * 订单交换机绑定订单释放队列
     *
     * @return
     */
    @Bean
    public Binding orderReleaseOrderBinding() {

        return new Binding(
                MQ_ORDER_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_ORDER_EXCHANGE,
                MQ_ORDER_RELEASE_ROUTINGKEY,
                null);
    }

    /**
     * 订单交换机绑定库存释放队列
     *
     * @return
     */
    @Bean
    public Binding wareReleaseStockBinding() {
        return new Binding(
                MQ_STOCK_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_ORDER_EXCHANGE,
                MQ_ORDERTOWARE_ROUTINGKEY,
                null);
    }

    /**
     * 秒杀队列
     */
    @Bean
    public Queue secKillOrderCreateQueue() {
        Queue queue = new Queue(MQ_ORDER_SECKILL_QUEUE,
                true,
                false,
                false,
                null
        );
        return queue;
    }

    /**
     * 创建秒杀订单绑定
     */
    @Bean
    public Binding seckillOrderCreateBinding() {
        return new Binding(
                MQ_ORDER_SECKILL_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_ORDER_EXCHANGE,
                MQ_SECKILL_ORDER_CREATE_ROUTINGKEY,
                null);
    }



}
