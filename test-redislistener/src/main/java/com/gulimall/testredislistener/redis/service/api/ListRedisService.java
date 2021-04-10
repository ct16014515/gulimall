package com.gulimall.testredislistener.redis.service.api;

import java.util.List;

public interface ListRedisService {

    public List<Object> lGet(String key, long start, long end);

    public long lGetListSize(String key);

    public Object lGetIndex(String key, long index);

    public boolean lSet(String key, Object value);

    public boolean lSet(String key, Object value, long time);

    public boolean lSet(String key, List<Object> value);

    public boolean lSet(String key, List<Object> value, long time);

    public boolean lUpdateIndex(String key, long index, Object value);

    public long lRemove(String key, long count, Object value);
}
