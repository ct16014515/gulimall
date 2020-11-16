package com.iflytek.gulimall.order;

import com.iflytek.gulimall.order.config.AlipayTemplate;
import com.iflytek.gulimall.order.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
class GulimallOrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void createExchange() {
        DirectExchange directExchange = new DirectExchange(
                "hello.java.exchange",
                true,
                false);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("交换机创建成功" + directExchange);
    }

    @Test
    public void createQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete
        Queue queue = new Queue(
                "hello.java.queue",
                true,
                false,
                false);
        amqpAdmin.declareQueue(queue);
        System.out.println("队列创建成功" + queue);
    }

    @Test
    public void createBinding() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments
        Binding binding = new Binding(
                "hello.java.queue",
                Binding.DestinationType.QUEUE,
                "hello.java.exchange",
                "hello.java",
                null);
        amqpAdmin.declareBinding(binding);
        System.out.println("绑定创建成功" + binding);
    }

    @Test
    public void sendMessage() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setBillContent("你好");
        rabbitTemplate.convertAndSend("hello.java.exchange",
                "hello.java",
                order);
        System.out.println("消息发送成功");
    }
    @Autowired
    AlipayTemplate alipayTemplate;

    @Test
    public void colseOrder(){
        alipayTemplate.closeOrder("1315586711895453697");
    }


    @Test
    public void queryOrder(){
        alipayTemplate.queryOrder("1315585490858061826");
    }

}
