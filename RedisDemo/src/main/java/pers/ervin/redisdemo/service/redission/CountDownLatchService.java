package pers.ervin.redisdemo.service.redission;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CountDownLatchService {
    @Autowired
    private RedissonClient redissonClient;

    //主线程等待所有子线程完成
    public void await(){
        try {
            RCountDownLatch latch = redissonClient.getCountDownLatch("latch");
            latch.trySetCount(3);//设置计数器初始大小
            long count = latch.getCount();
            System.out.println("count = " + count);
            latch.await();//阻塞线程直到计数器归零
            System.out.println(Thread.currentThread().getName()+"所有子线程已运行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //子线程
    public void thread(){
        try {
            RCountDownLatch latch = redissonClient.getCountDownLatch("latch");
            System.out.println(Thread.currentThread().getName()+"抵达现场");
            TimeUnit.SECONDS.sleep(1);
            latch.countDown();//计数器减1，当计数器归零后通知所有等待着的线程恢复执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

