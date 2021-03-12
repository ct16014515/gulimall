package com.iflytek.gulimall.seckill.controller;

import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.feign.SeckillServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SecSessionSkuVO;
import com.iflytek.gulimall.common.feign.vo.SeckillSkuVO;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.seckill.service.SecKillService;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


@Controller
public class SecKillController implements SeckillServiceAPI {

    @Autowired
    SecKillService secKillService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @GetMapping("/seckill/getSecSessionSkuVOBySkuId/{skuId}")
    @ResponseBody
    public ResultBody<SecSessionSkuVO> getSecSessionSkuVOBySkuId(@PathVariable("skuId") Long skuId) {
        SecSessionSkuVO secSessionSkuVO = secKillService.getSecSessionSkuVOBySkuId(skuId);
        return new ResultBody<>(secSessionSkuVO);
    }

    /**
     * 查询时间进行的秒杀活动和商品
     *
     * @return
     */
    @GetMapping("/seckill/getCurrentSecSessionSkuVOS")
    @ResponseBody
    public ResultBody<List<SecSessionSkuVO>> getCurrentSecSessionSkuVOS() {
        List<SecSessionSkuVO> vos = secKillService.getCurrentSecSessionSkuVOS();
        return new ResultBody<>(vos);
    }

    /**
     * 秒杀
     *
     * @param key
     * @param randomCode
     * @param number
     * @return TODO  秒杀结束后生成订单号,消息队列监听消息创建订单,然后添加收货地址,支付业务
     */
    @GetMapping("/kill")
    public String kill(@RequestParam("key") String key,
                       @RequestParam("randomCode") String randomCode,
                       @RequestParam("number") Integer number,
                       Model model) {

        String orderSn = secKillService.kill(key, randomCode, number);
        model.addAttribute("orderSn", orderSn);
        return "success";
    }




}
