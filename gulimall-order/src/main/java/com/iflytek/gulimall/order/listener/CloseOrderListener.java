package com.iflytek.gulimall.order.listener;

import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.iflytek.common.constant.OrderConstant.MQ_ORDER_RELEASE_QUEUE;

@Service
@RabbitListener(queues = {MQ_ORDER_RELEASE_QUEUE})
@Slf4j
public class CloseOrderListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void orderReleaseListener(OrderEntity entity, Message message, Channel channel) throws IOException {
        log.info("*******收到释放订单消息,订单号:{}************", entity.getOrderSn());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            orderService.closeOrder(entity);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, true);
        }
    }


}
