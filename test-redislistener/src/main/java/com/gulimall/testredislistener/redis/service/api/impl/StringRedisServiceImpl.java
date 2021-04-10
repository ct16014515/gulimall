package com.gulimall.testredislistener.redis.service.api.impl;

import com.gulimall.testredislistener.redis.service.api.StringRedisService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j(topic = "StringRedisServiceImpl")
public class StringRedisServiceImpl implements StringRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final long EXPIRE = 1000L;

    private static final long TIMEOUT = 1000L;

    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value, long time) {
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long incr(String key, long delta) {
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long decr(String key, long delta) {
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object get(String key) {
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key){
        redisTemplate.delete(key);
    }

    @Override
    public boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean lock(String key,String value){
        log.info("获取锁 kye:{},value:{}",key,value);
        //请求锁时间
        long requestTime = System.currentTimeMillis();
        while (true){
            //等待锁时间
            long watiTime = System.currentTimeMillis() - requestTime;
            //如果等待锁时间超过10s，加锁失败
            if(watiTime > TIMEOUT){
                log.info("等待锁超时 kye:{},value:{}",key,value);
                return false;
            }

            if(redisTemplate.opsForValue().setIfAbsent(key,String.valueOf(System.currentTimeMillis()))){
                //获取锁成功
                log.info("获取锁成功 kye:{},value:{}",key,value);
                //设置超时时间，防止解锁失败，导致死锁
                redisTemplate.expire(key,EXPIRE, TimeUnit.MILLISECONDS);
                return true;
            }

            String valueTime = (String) redisTemplate.opsForValue().get(key);
            if(! StringUtils.isEmpty(valueTime) && System.currentTimeMillis() - Long.parseLong(valueTime) > EXPIRE){
                //加锁时间超过过期时间，删除key，防止死锁
                log.info("锁超时, key:{}, value:{}", key, value);
                try{
                    redisTemplate.opsForValue().getOperations().delete(key);
                }catch (Exception e){
                    log.info("删除锁异常 key:{}, value:{}", key, value);
                    e.printStackTrace();
                }
                return false;
            }

            //获取锁失败，等待20毫秒继续请求
            try {
                log.info("等待20 nanoSeconds key:{},value:{}",key,value);
                TimeUnit.NANOSECONDS.sleep(1000);
            } catch (InterruptedException e) {
                log.info("等待20 nanoSeconds 异常 key:{},value:{}",key,value);
                e.printStackTrace();
            }
        }
    }
    /**
     * 分布式加锁
     * @param key
     * @param value
     * @return
     * */
    @Override
    public boolean secKilllock(String key,String value){
        /**
         * setIfAbsent就是setnx
         * 将key设置值为value，如果key不存在，这种情况下等同SET命令。
         * 当key存在时，什么也不做。SETNX是”SET if Not eXists”的简写
         * */
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            //加锁成功返回true
            return true;
        }
        //避免死锁，且只让一个线程拿到锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        /**
         * 下面这几行代码的作用：
         * 1、防止死锁
         * 2、防止多线程抢锁
         * */
        if(! StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()){
            //如果锁过期了,获取上一个锁的时间
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key,value);
            //只会让一个线程拿到锁
            //如果旧的value和currentValue相等，只会有一个线程达成条件，因为第二个线程拿到的oldValue已经和currentValue不一样了
            if(! StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }
    /**
     * 解锁
     * @param key
     * @param value
     * */
    @Override
    public void unlock(String key,String value){
        try{
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("『redis分布式锁』解锁异常，{}", e);
        }
    }

    /**
     * 清空所有key
     */
    @Override
    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }
}
