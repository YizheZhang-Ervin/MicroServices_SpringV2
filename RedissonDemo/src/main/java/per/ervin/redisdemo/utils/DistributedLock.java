package per.ervin.redisdemo.utils;

import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;


public class DistributedLock {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    public final static String LOCKER_PREFIX = "lock:";
    public final static String LOCKER_GOODS_CONSUME = "GOODS_CONSUME";

    public final static Long LOCKER_WAITE_TIME = 10L;

    public final static Long LOCKER_LOCK_TIME = 100L;

    @Value("${server.port}")
    Integer serverId;

    @Value("${server.useLock}")
    boolean useLock = true;

    @Resource
    protected RedissonClient redissonClient;

    public String tryLock(LockWorker lockWorker) {
        try {
            if (useLock) {
                log.info("当前server 节点为：{}", serverId);
                log.info("开始获取lock key:{}", LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                RLock lock = redissonClient.getFairLock(LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                log.info("创建lock key:{}，尝试lock", LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                // (公平锁)最多等待10秒，锁定后经过lockTime秒后自动解锁
                boolean success = lock.tryLock(LOCKER_WAITE_TIME, LOCKER_LOCK_TIME, TimeUnit.SECONDS);
                if (success) {
                    try {
                        log.info("成功到获取lock key:{}", LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                        return lockWorker.invoke();
                    } finally {
                        log.info("释放lock key:{}", LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                        lock.unlock();
                    }
                }
                log.info("获取lock key:{}失败", LOCKER_PREFIX + LOCKER_GOODS_CONSUME);
                return "获取分布式锁失败！";
            } else {
                log.info("当前server 节点为：{}", serverId);
                log.info("当前server 没有使用分布式锁：{}",serverId);
                return lockWorker.invoke();
            }
        } catch (Exception e) {
            log.error("获取lock key异常", e);
            return "获取分布式锁过程异常！";
        }
    }
}