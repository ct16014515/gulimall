package com.iflytek.gulimall.order.app;

import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.entity.OrderItemEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class RabbitmqController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage")
    public String sendMessage() {

        OrderEntity order = new OrderEntity();
        order.setOrderSn(UUID.randomUUID().toString());
        order.setModifyTime(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange",
                "order.create.order",
                order);

        return "ok";
    }

}
