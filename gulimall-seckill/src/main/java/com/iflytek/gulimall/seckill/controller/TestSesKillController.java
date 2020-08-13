package com.iflytek.gulimall.seckill.controller;

import com.iflytek.common.utils.ResultBody;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestSesKillController {

    @Autowired
    RedissonClient redissonClient;
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    /**
     * 设置库存
     *
     * @return
     */
    @GetMapping("/setStock")
    public ResultBody setStock() {
        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + "1");
        semaphore.trySetPermits(10);
        return new ResultBody(100, "success", null);
    }

    @GetMapping("/kill")
    public ResultBody kill() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + "1");
        boolean b = semaphore.tryAcquire(1, 100, TimeUnit.MILLISECONDS);
        if (b) {
            System.out.println("秒杀成功");
            /**
             * 发送消息队列
             */
            return new  ResultBody(100, "success", null);
        }else {
            return new  ResultBody(100, "error", null);
        }
    }


}
