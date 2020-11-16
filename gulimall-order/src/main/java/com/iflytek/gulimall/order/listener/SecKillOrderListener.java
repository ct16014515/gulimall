package com.iflytek.gulimall.order.listener;

import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.model.mq.to.SecKillOrderCreateTO;
import com.iflytek.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.iflytek.gulimall.common.constant.MqConstant.MQ_ORDER_SECKILL_QUEUE;

@Service
@RabbitListener(queues = {MQ_ORDER_SECKILL_QUEUE})
@Slf4j
public class SecKillOrderListener {

    @Autowired
    MqServiceAPI mqServiceAPI;
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void createSecKillOrder(SecKillOrderCreateTO entityTO,Message message, Channel channel ) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
        try {

            log.info("收到创建秒杀订单的消息,订单号:[{}]",entityTO.getOrderSn());
            orderService.createSecKillOrder(entityTO);
            channel.basicAck(deliveryTag, false);
            mqServiceAPI.updateMessageStatus(messageId);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(deliveryTag, true);
        }
    }
}






