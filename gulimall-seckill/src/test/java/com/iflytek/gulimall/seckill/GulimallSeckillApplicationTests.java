package com.iflytek.gulimall.seckill;

import com.iflytek.gulimall.seckill.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class GulimallSeckillApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;




    @Test
    public void sendMessage() {
        OrderVO order = new OrderVO();
        order.setId("1");
        order.setName("张三");
        rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java=routingKey", order);


    }

    /**
     * 1、如何创建Exchange、Queue、Binding
     * 1）、使用AmqpAdmin进行创建
     * 2、如何收发消息
     */
    @Test
    public void createExchange() {

        Exchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功：", "hello-java-exchange");
    }

    /**
     * 创建队列
     */
    @Test
    public void createQueue() {
        /**
         * String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
         */
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功：", "hello-java-queue");
    }

    /**
     * 创建绑定
     */
    @Test
    public void createBinding() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments

        Binding binding = new Binding(
                "hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello-java=routingKey",
                null);
        amqpAdmin.declareBinding(binding);
        log.info("创建成功绑定");
    }



}
