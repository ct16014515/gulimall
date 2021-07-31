package com.iflytek.gulimall.order.listener;

import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.model.mq.to.OrderEntityReleaseTO;
import com.iflytek.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.iflytek.gulimall.common.constant.MqConstant.MQ_ORDER_RELEASE_QUEUE;


@Service
@RabbitListener(queues = {MQ_ORDER_RELEASE_QUEUE})
@Slf4j
public class CloseOrderListener {

    @Autowired
    private OrderService orderService;
    @Autowired
    MqServiceAPI mqServiceAPI;

    @RabbitHandler
    public void orderReleaseListener(OrderEntityReleaseTO entityTO, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
        try {
            orderService.closeOrder(entityTO);
            channel.basicAck(deliveryTag, false);
            mqServiceAPI.updateMessageStatus(messageId);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(deliveryTag, false);
        }
    }


}
