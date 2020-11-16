package com.iflytek.gulimall.ware.listener;

import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.model.mq.to.OrderEntityPayedTO;
import com.iflytek.gulimall.common.model.mq.to.OrderEntityReleaseTO;
import com.iflytek.gulimall.common.model.mq.to.WareStockDelayTO;

import com.iflytek.gulimall.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.iflytek.gulimall.common.constant.MqConstant.MQ_STOCK_RELEASE_QUEUE;


@RabbitListener(queues = {MQ_STOCK_RELEASE_QUEUE})
@Service
@Slf4j
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;
    @Autowired
    MqServiceAPI mqServiceAPI;
    @Async
    @RabbitHandler
    public void releaseStock(WareStockDelayTO wareStockDelayTO, Message message, Channel channel) throws IOException {
        try {
            String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
            wareSkuService.stockRelease(wareStockDelayTO);
            mqServiceAPI.updateMessageStatus(messageId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("出现异常:{},消息重新入队列,订单号:{}",e, wareStockDelayTO.getOrderSn());
            //出现异常,重新返回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
    @Async
    @RabbitHandler
    public void releaseStock(OrderEntityReleaseTO orderEntityReleaseTO, Message message, Channel channel) throws IOException {
        try {
            String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
            wareSkuService.stockRelease(orderEntityReleaseTO);
            mqServiceAPI.updateMessageStatus(messageId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("出现异常,消息重新入队列,订单号:{}", orderEntityReleaseTO.getOrderSn());
            //出现异常,重新返回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
    @Async
    @RabbitHandler
    public void reduceStock(OrderEntityPayedTO orderEntityPayedTO, Message message, Channel channel) throws IOException {
        try {
            String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
            wareSkuService.stockReduce(orderEntityPayedTO);
            mqServiceAPI.updateMessageStatus(messageId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("出现异常,消息重新入队列,订单号:{}", orderEntityPayedTO.getOrderSn());
            //出现异常,重新返回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


}
