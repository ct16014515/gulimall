package com.iflytek.gulimall.order.config;

import com.iflytek.common.constant.WareConstant;
import com.iflytek.gulimall.order.entity.OrderEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.iflytek.common.constant.OrderConstant.*;
import static com.iflytek.common.constant.WareConstant.MQ_STOCK_RELEASE_QUEUE;

@Configuration
public class MyQueueConfig {


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
        argument.put("x-message-ttl", 60 * 1000);
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
     * @return
     */
    @Bean
    public Binding wareReleaseStockBinding() {
        return new Binding(
                MQ_STOCK_RELEASE_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_ORDER_EXCHANGE,
                MQ_ORDERTOSTOCK_RELEASE_ROUTINGKEY,
                null);
    }


}
