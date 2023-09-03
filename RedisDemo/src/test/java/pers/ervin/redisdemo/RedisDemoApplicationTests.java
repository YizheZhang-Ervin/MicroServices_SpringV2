package pers.ervin.redisdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pers.ervin.redisdemo.service.SecKillService;
import pers.ervin.redisdemo.task.SecKillTask;
import pers.ervin.redisdemo.task.TestTask;
import pers.ervin.redisdemo.utils.RedissionUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisDemoApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    RedissionUtil redissionUtil;
    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;
    @Resource
    SecKillService secKillService;

    final static String KEY = "test";
    final static String VALUE = "123abc";
    final static String REDISSON_KEY = "redisson_key";

    @Test
    void setValue() {
        redisTemplate.opsForValue().set(KEY,VALUE);
    }

    @Test
    void getValue(){
        Object value = redisTemplate.opsForValue().get(KEY);
        System.out.println(value);
    }

    // 测试锁
    @Test
    public void test4() {
        try {
            String str1 = String.format("线程访问开始: %s",Thread.currentThread().getName());
            System.out.println(str1);
            // 尝试获取锁，等待3秒，自己获得锁后一直不解锁则5秒后自动解锁
            boolean lock = redissionUtil.tryLock(REDISSON_KEY, TimeUnit.SECONDS, 3L, 5L);
            if (lock) {
                String str2 = String.format("线程:%s，获取到了锁",Thread.currentThread().getName());
                System.out.println(str2);
                // 获得锁之后可以进行相应的处理  睡一会
                Thread.sleep(100);
                String str3 = String.format("%s获得锁后进行相应的操作",Thread.currentThread().getName());
                System.out.println(str3);
                redissionUtil.unlock(REDISSON_KEY);
            }
        } catch (Exception e) {
            System.out.println("错误信息：{}" + e.toString());
            System.out.println("线程：{} 获取锁失败" + Thread.currentThread().getName());
        }
    }

    // 测试线程池
    @Test
    public void test5() throws ExecutionException, InterruptedException {
        List<String> strings = Arrays.asList("Spider man", "Haoke", "Mary", "Lilei", "Han Meimei");
        long start = System.currentTimeMillis();
        List<String> resultList = new ArrayList<>();
        List<Future<Map<String,String>>> futureList = new ArrayList<>();
        for (String string : strings) {
            TestTask testTask = new TestTask(string);
            Future<Map<String,String>> result = this.taskExecutor.submit(testTask);
            futureList.add(result);
            String str1 = String.format("A 添加到结果:%s,结果时间:%s",string,LocalDateTime.now());
            System.out.println(str1);
        }
        // future.get() 会阻塞调用线程（主线程），如果在上面的循环中获取，整个服务就会变成并行，失去使用线程池的意义
        for (Future<Map<String,String>> resultFuture : futureList) {
            Map<String,String> data = resultFuture.get();
            String str2 = String.format("B 获取到future结果:%s,结果时间:%s",data.get("data"),LocalDateTime.now());
            System.out.println(str2);
            resultList.add(data.get("data"));
        }
        long end = System.currentTimeMillis();
        String str3 = String.format("C 程序执行时间:%s",end - start);
        System.out.println(str3);
        String str4 = String.format("D 结果集:%s",resultList.toString());
        System.out.println(str4);
    }

    // 用线程池测试锁
    @Test
    public void test6() throws ExecutionException, InterruptedException {
        // 虚拟出100个用户购买商品
        Map<String,String> userGoodMap = new HashMap<>();
        for(int i=0;i<100;i++){
            Random random = new Random();
            // nextInt随机出来的数字是0~(bound-1)
            userGoodMap.put("user"+i,String.valueOf(random.nextInt(5)+1));
        }
        // 任务&结果列表
        List<String> resultList = new ArrayList<>();
        List<Future<String>> futureList = new ArrayList<>();
        // 跑任务
        for (String user : userGoodMap.keySet()) {
            SecKillTask secKillTask = new SecKillTask(user,userGoodMap.get(user),secKillService);
            Future<String> result = this.taskExecutor.submit(secKillTask);
            futureList.add(result);
        }
        // 收集结果
        for (Future<String> resultFuture : futureList) {
            resultList.add(resultFuture.get());
        }
        String str = String.format("D 结果集:%s",resultList.toString());
        System.out.println(str);
    }
}
