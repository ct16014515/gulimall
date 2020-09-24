package com.iflytek.gulimall.autherserver.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.support.collections.RedisMap;

import java.io.IOException;

@Configuration
@ConfigurationProperties("spring.redis")
@Data
public class MyRedissonConfig {

    private int database;
    private String port;
    private String host;
    private String password;


    /**
     * 所有对Redisson的使用都是通过RedissonClient
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient()  {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port).setDatabase(database).setPassword(password);
        //2、根据Config创建出RedissonClient实例0
        //Redis url should start with redis:// or rediss://
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
