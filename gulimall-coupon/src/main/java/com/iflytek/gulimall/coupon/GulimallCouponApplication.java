package com.iflytek.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.iflytek.gulimall.coupon","com.iflytek.gulimall.common.feign"})
@MapperScan("com.iflytek.gulimall.coupon.dao")
@EnableDiscoveryClient
@EnableAsync
@EnableRabbit
@EnableFeignClients(basePackages = {"com.iflytek.gulimall.common.feign"})
public class GulimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }

}
