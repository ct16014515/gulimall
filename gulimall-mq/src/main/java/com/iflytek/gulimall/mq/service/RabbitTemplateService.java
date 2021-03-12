package com.iflytek.gulimall.mq.service;

import com.alibaba.fastjson.JSON;

import com.iflytek.gulimall.common.feign.vo.SendMessageRequest;
import com.iflytek.gulimall.mq.dao.MqMessageDao;
import com.iflytek.gulimall.mq.entity.MqMessage;
import com.iflytek.gulimall.mq.enume.MqMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RabbitTemplateService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    MqMessageDao mqMessageDao;

    /**
     * 发送消息,给每个消息做好日志存储在mongoDB处理
     * @param sendMessageRequest
     * @throws ClassNotFoundException
     */
    public void sendMessage(SendMessageRequest sendMessageRequest) throws ClassNotFoundException {
        /**
         * String exchange, String routingKey, final Object object,
         * @Nullable CorrelationData correlationData
         */
        MqMessage mqMessage = new MqMessage();
        String timeId = UUID.randomUUID().toString().replaceAll("-", "") + System.currentTimeMillis();
        CorrelationData correlationData = new CorrelationData(timeId);
        mqMessage.setMessageId(timeId);
        String className = sendMessageRequest.getClassName();
        mqMessage.setClassType(className);
        Object object = sendMessageRequest.getObject();
        String content = JSON.toJSONString(object);
        mqMessage.setContent(content);
        if (!StringUtils.isEmpty(className)){
            Class<?> clazz = Class.forName(className);
            object= JSON.parseObject(content, clazz);
        }
        mqMessage.setToExchange(sendMessageRequest.getExchange());
        mqMessage.setRoutingKey(sendMessageRequest.getRoutingKey());
        mqMessage.setCreateTime(LocalDateTime.now());
        mqMessage.setUpdateTime(LocalDateTime.now());
        try {
            mqMessage.setMessageStatus(MqMessageEnum.CREATE_NEW.getCode());
            rabbitTemplate.convertAndSend(sendMessageRequest.getExchange(), sendMessageRequest.getRoutingKey(), object, correlationData);
        } catch (Exception e) {
            mqMessage.setMessageStatus(MqMessageEnum.ERROR_ARRIVE.getCode());
        }
        mqMessageDao.insert(mqMessage);
    }



    public void updateMessageStatus(String messageId) {
        Optional<MqMessage> optionalMqMessage = mqMessageDao.findById(messageId);
        if (optionalMqMessage.isPresent()) {
            MqMessage mqMessage = optionalMqMessage.get();
            mqMessage.setMessageStatus(MqMessageEnum.OK_ARRIVE.getCode());
            mqMessage.setUpdateTime(LocalDateTime.now());
            mqMessageDao.save(mqMessage);
        }
    }


}
