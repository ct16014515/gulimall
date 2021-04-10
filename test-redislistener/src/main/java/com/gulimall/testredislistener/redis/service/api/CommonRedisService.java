package com.gulimall.testredislistener.redis.service.api;

public interface CommonRedisService {

    public boolean lock(String key);

    public void delete(String key);

    public boolean expire(String key, long time);

    public long getExpire(String key);

    public boolean hasKey(String key);

    public void del(String... key);
}
