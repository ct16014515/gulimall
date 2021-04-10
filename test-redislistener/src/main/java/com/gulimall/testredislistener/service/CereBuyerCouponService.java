package com.gulimall.testredislistener.service;

        import com.gulimall.testredislistener.entity.Person;
        import com.gulimall.testredislistener.redis.service.api.StringRedisService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.cache.annotation.Cacheable;
        import org.springframework.stereotype.Service;

@Service
public class CereBuyerCouponService {
    @Autowired
    private StringRedisService stringRedisService;
    @Cacheable(cacheNames = "CereBuyerCouponService",keyGenerator="keyGenerator")
    public Person createOrder() {
        stringRedisService.set("order",new Person("张三"),1000*60);
        return new Person("张三");
    }
}
