package com.iflytek.gulimall.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class MyrabbitmqConfig {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * ┌─────┐
     * |  rabbitTemplate defined in class path resource [org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$RabbitTemplateConfiguration.class]
     * ↑     ↓
     * |  rabbitTemplateConfigurer defined in class path resource [org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$RabbitTemplateConfiguration.class]
     * ↑     ↓
     * |  myrabbitmqConfig (field private org.springframework.amqp.rabbit.core.RabbitTemplate com.iflytek.gulimall.order.config.MyrabbitmqConfig.rabbitTemplate)
     * └─────┘
     * 由于RabbitTemplateConfiguration会调用MyrabbitmqConfig的bean->MessageConverter
     * MyrabbitmqConfig会调用RabbitTemplateConfiguration的bean->RabbitTemplate
     * 导致循环依赖
     * @return
     */
   // @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 可靠消息,发送端确认
     * 生产者---[ConfirmCallback(true)]-->broker(exchange)------>queue
     * 1、ConfirmCallback,消息成功抵达broker[exchange]就会回调 需要配置spring.rabbitmq.publisher-confirm-type: correlated
     * 2、ReturnCallback消息没有抵达队列会触发回调，比如此routeKey在exchange路由时没有匹配到合适的队列
     * 设置 spring.rabbitmq.publisher-returns: true   spring.rabbitmq.template.mandatory: true
     */
    @PostConstruct
    public void initRabbitMq() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
              log.info("消息成功抵达broker-------->correlationData:{},ack:{},cause:{}",correlationData,ack,cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息没有抵达队列----->message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",
                        message,replyCode,replyText,exchange,routingKey);
            }
        });

    }


}
