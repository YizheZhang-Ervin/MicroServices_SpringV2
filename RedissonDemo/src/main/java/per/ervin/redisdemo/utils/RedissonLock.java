package per.ervin.redisdemo.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedissonLock {
    @Resource
    private RedissonClient redissonClient;

    public void updateUser(String userId) {
        String lockKey = "config" + userId;
        RLock rLock = redissonClient.getLock(lockKey);  //获取锁资源
        try {
            //rLock.lock(10, TimeUnit.SECONDS);   //加锁,可以指定锁定时间
            rLock.lock(); //拿锁失败时会不停的重试;具有Watch Dog 自动延期机制 默认续30s 每隔30/3=10 秒续到30s
            // 业务代码
            System.out.println("test");
        } finally {
            rLock.unlock();   //释放锁
        }
    }
}

