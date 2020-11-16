package com.iflytek.gulimall.member;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;



@MapperScan("com.iflytek.gulimall.member.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iflytek.gulimall.common.feign"})
@EnableRedisHttpSession
@SpringBootApplication(scanBasePackages = {"com.iflytek.gulimall.member","com.iflytek.gulimall.common.feign"})
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
