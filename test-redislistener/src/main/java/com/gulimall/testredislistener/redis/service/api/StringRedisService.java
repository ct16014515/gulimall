package com.gulimall.testredislistener.redis.service.api;

public interface StringRedisService {

    public boolean set(String key, Object value);

    public boolean set(String key, Object value, long time);

    public long incr(String key, long delta);

    public long decr(String key, long delta);

    public Object get(String key);

    public void delete(String key);

    public boolean isExist(String key);

    public boolean lock(String key, String value);

    public boolean secKilllock(String key, String value);

    public void unlock(String key, String value);

    public void deleteAll();
}
