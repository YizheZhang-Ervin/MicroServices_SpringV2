package pers.ervin.redisdemo.service.redission;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.ervin.redisdemo.utils.RedissionUtil;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class LockService {
    public static int amount = 5;
    public final static String REDISSON_KEY = "redisson_key";

    @Autowired
    public RedissionUtil redissionUtil;

    @Autowired
    private RedissonClient redissonClient;

    //没获取到锁阻塞线程
    public Integer test1() {
        RLock lock = null;
        try {
            // 创建一个名字为lock的锁,如果是并发访问,会阻塞到 lock.lock();,知道2秒后,才能执行下面的逻辑代码
            lock = redissonClient.getLock("lock");
            lock.lock();
            System.out.println(formatDate() + " " + Thread.currentThread().getName() + "获取到锁");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != lock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return amount;
    }

    //立刻返回获取锁的状态
    public Integer test2() {
        RLock lock = null;
        try {
            lock = redissonClient.getLock("lock");
            // 判断获取锁,执行业务逻辑,否则直接返回提示信息
            if (lock.tryLock()) {
                System.out.println(formatDate() + " " + Thread.currentThread().getName() + "获取到锁");
                Thread.sleep(2000);
            } else {
                System.out.println(formatDate() + " " + Thread.currentThread().getName() + "已抢光");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != lock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return amount;
    }

    //立刻返回获取锁的状态
    public Integer test3() {
        RLock lock = redissonClient.getLock("lock"); //非公平锁,随机取一个等待中的线程分配锁
        //RLock lock=redissonClient.getFairLock("lock"); //公平锁,按照先后顺序依次分配锁
        try {
            if (lock.tryLock(2, 10, TimeUnit.SECONDS)) { //最多等待锁2秒，10秒后强制解锁,推荐使用
                System.out.println(formatDate() + " " + Thread.currentThread().getName() + "获取到锁");
                Thread.sleep(4500);
            } else {
                System.out.println(formatDate() + " " + Thread.currentThread().getName() + "未获取到锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != lock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return amount;
    }

    // 锁
    public void test4() {
        try {
            System.out.println("============={} 线程访问开始============" + Thread.currentThread().getName());
            // 尝试获取锁，等待3秒，自己获得锁后一直不解锁则5秒后自动解锁
            boolean lock = redissionUtil.tryLock(REDISSON_KEY, TimeUnit.SECONDS, 3L, 5L);
            if (lock) {
                System.out.println("线程:{}，获取到了锁" + Thread.currentThread().getName());
                // 获得锁之后可以进行相应的处理  睡一会
                Thread.sleep(100);
                System.out.println("======获得锁后进行相应的操作======" + Thread.currentThread().getName());
                redissionUtil.unlock(REDISSON_KEY);
                System.out.println("=============================" + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            System.out.println("错误信息：{}" + e.toString());
            System.out.println("线程：{} 获取锁失败" + Thread.currentThread().getName());
        }
    }

    public String formatDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.SECOND) + ":" + c.get(Calendar.MILLISECOND);
    }
}

