package pers.ervin.redisdemo.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissionUtil {
    @Autowired
    private RedissonClient redissonClient; // RedissonClient已经由配置类生成，这里自动装配即可

    /**
     * 锁住不设置超时时间(拿不到lock就不罢休，不然线程就一直block)
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * leaseTime为加锁时间，单位为秒
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return null;
    }

    /**
     * timeout为加锁时间，时间单位由unit确定
     */
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 通过lockKey解锁
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 直接通过锁解锁
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }
}
