package pers.ervin.redisdemo.service.redission;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 限流器
 * 1.先调用init方法生成5个令牌
 * 2.通过该限流器的名称rateLimiter来获取令牌limiter.tryAcquire()
 * 3.谁抢到,谁先执行,否则返回提示信息,可以用于秒杀场景
 *
 * */
public class RateLimiterService {
    @Autowired
    private RedissonClient redissonClient;

    //初始化限流器
    public void init() {
        RRateLimiter limiter = redissonClient.getRateLimiter("rateLimiter");
        limiter.trySetRate(RateType.PER_CLIENT, 5, 1, RateIntervalUnit.SECONDS);//每1秒产生5个令牌
    }

    //获取令牌
    public void thread() {
        RRateLimiter limiter = redissonClient.getRateLimiter("rateLimiter");
        if (limiter.tryAcquire()) {//尝试获取1个令牌
            System.out.println(Thread.currentThread().getName() + "成功获取到令牌");
        } else {
            System.out.println(Thread.currentThread().getName() + "未获取到令牌");
        }
    }
}