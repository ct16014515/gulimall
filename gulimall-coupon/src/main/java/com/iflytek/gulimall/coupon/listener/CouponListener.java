package com.iflytek.gulimall.coupon.listener;

import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.coupon.service.SeckillSessionService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

import static com.iflytek.gulimall.common.constant.MqConstant.MQ_JOB_SECKILLPRODUCTUP_QUEUE;

@Service
@Slf4j
public class CouponListener {


    @Autowired
    private SeckillSessionService seckillSessionService;
    @Autowired
    private MqServiceAPI mqServiceAPI;

    /**
     * 监听秒杀商品上架的消息队列
     * @param message
     * @param channel
     * @param param
     * @throws IOException
     */
    @RabbitListener(queues = MQ_JOB_SECKILLPRODUCTUP_QUEUE)
    @Async
    public void uploadSeckillProductLast3Days(Message message, Channel channel,String param) throws IOException {
        try {
            log.info("收到秒杀商品上架的消息,参数{}",param);
            String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
            seckillSessionService.uploadSeckillProductLast3Days();
            mqServiceAPI.updateMessageStatus(messageId);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
