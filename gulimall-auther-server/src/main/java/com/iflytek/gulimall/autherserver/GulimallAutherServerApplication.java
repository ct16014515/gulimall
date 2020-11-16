package com.iflytek.gulimall.autherserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication(scanBasePackages = {"com.iflytek.gulimall.autherserver","com.iflytek.gulimall.common.feign"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iflytek.gulimall.common.feign"})
@EnableRedisHttpSession
public class GulimallAutherServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallAutherServerApplication.class, args);
    }

}
