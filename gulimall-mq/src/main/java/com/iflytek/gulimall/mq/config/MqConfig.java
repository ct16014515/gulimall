package com.iflytek.gulimall.mq.config;

import com.iflytek.gulimall.mq.dao.MqMessageDao;
import com.iflytek.gulimall.mq.entity.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Configuration
@Slf4j
public class MqConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqMessageDao mqMessageDao;

    @PostConstruct
    public void initRabbitMq() {
        log.info("初始化mq增强版中************");
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                String messageId = correlationData.getId();
                log.info("消息成功抵达broker----->messageId:{}", messageId);

            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                String messageId = message.getMessageProperties().getHeader("spring_returned_message_correlation").toString();
                while (true) {
                    Optional<MqMessage> optional = mqMessageDao.findById(messageId);
                    if (optional.isPresent()) {
                        MqMessage mqMessage = null;
                        try {
                            mqMessage = optional.get();
                            mqMessage.setMessageStatus(2);
                            mqMessage.setUpdateTime(LocalDateTime.now());
                            mqMessage.setReplyCode(replyCode);
                            mqMessage.setReplyText(replyText);
                            log.info("消息没有抵达队列----->messageId:{},mongo数据库保存的mqMessage:{}", messageId, mqMessage);
                            mqMessageDao.save(mqMessage);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }

                        }
                    }

                }
            }
        });
    }
}
