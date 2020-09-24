package com.iflytek.gulimall.order;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import com.iflytek.gulimall.order.config.DataSourceProxyAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * RabbitListener可用于方法和类上
 * RabbitHandler只能用于方法上,当消息为不同的实体类,提供不同的重载方法
 *
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.iflytek.gulimall.order.dao")
@EnableDiscoveryClient
@EnableFeignClients
@EnableRedisHttpSession
@EnableRabbit
@Import({DataSourceProxyAutoConfiguration.class})
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }
    @Bean
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
