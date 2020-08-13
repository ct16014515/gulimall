package com.iflytek.gulimall.autherserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients//feign的客户端,远程调用使用
public class GulimallAutherServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallAutherServerApplication.class, args);
    }

}
