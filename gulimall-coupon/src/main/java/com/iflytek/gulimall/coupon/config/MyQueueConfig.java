package com.iflytek.gulimall.coupon.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.iflytek.gulimall.common.constant.MqConstant.*;

@Configuration
public class MyQueueConfig {

    @Bean
    public Queue jobSeckillProductQueue() {
        Queue queue = new Queue(MQ_JOB_SECKILLPRODUCTUP_QUEUE,
                true,
                false,
                false,
                null
        );
        return queue;
    }

    @Bean
    public Exchange jobEventExchange() {
        TopicExchange topicExchange = new TopicExchange(
                MQ_JOB_EXCHANGE, true, false);
        return topicExchange;
    }

    @Bean
    public Binding jobSeckillpProductUpBing() {
        return new Binding(
                MQ_JOB_SECKILLPRODUCTUP_QUEUE,
                Binding.DestinationType.QUEUE,
                MQ_JOB_EXCHANGE,
                MQ_JOB_SECKILLPRODUCTUP_ROUTINGKEY,
                null);
    }


}
