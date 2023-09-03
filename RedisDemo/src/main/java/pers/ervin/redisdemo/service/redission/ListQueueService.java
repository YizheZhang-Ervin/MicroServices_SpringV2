package pers.ervin.redisdemo.service.redission;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ListQueueService {
    @Autowired
    private RedissonClient redissonClient;


    /**
     * //List测试 - 添加元素
     * http://127.0.0.1:8080/collection//list/add?a="2"
     * http://127.0.0.1:8080/collection//list/add?a="2"
     * 1. 可添加相同名称的元素
     *
     * @param a
     * @return
     */
    public List<String> addAndGetList(String a) {
        RList<String> list = redissonClient.getList("my_list");
        list.add(a);
        return list.readAll();
    }


    /**
     * //List测试 - 删除元素
     * http://127.0.0.1:8080/collection//list/del?a="亚春2"
     * 1 removeAll():删除值相同的多个元素
     * 2 remove()删除元素,仅删除匹配到的第一个元素
     *
     * @param a
     * @return
     */
    public List<String> removeList(String a) {
        RList<String> list = redissonClient.getList("my_list");
      /* 自定义删除条件
      list.removeIf(new Predicate<String>() {
          @Override
          public boolean test(String s) {
              return s.length()>10;
          }
      });*/

        //list.remove(a);//删除元素,仅删除匹配到的第一个元素
        list.removeAll(Arrays.asList(a));//删除指定集合中所有元素
        return list.readAll();
    }

    //Queue测试 - 添加元素
    public List<String> addQueue(String a) {
        RQueue<String> list = redissonClient.getQueue("my_queue");
        list.add(a);//添加一个元素到集合最末尾
        return list.readAll();
    }

    //Queue测试 - 读取元素
    public String pollQueue() {
        RQueue<String> list = redissonClient.getQueue("my_queue");
        return list.poll();//从队列的头部获取一个元素并从队列中删除该元素，队列为空时返回null
    }

    //Blocking Queue测试 - 添加元素
    public List<String> addBlockingQueue(String a) {
        RBlockingQueue<String> list = redissonClient.getBlockingQueue("my_blocking_queue");
        list.add(a);
        return list.readAll();
    }

    //Blocking Queue测试 - 读取元素
    public String getBlockingQueue() throws InterruptedException {
        RBlockingQueue<String> list = redissonClient.getBlockingQueue("my_blocking_queue");
        //return list.poll();//从队列的头部获取一个元素并从队列中删除该元素，队列为空时返回null
        return list.take();//从队列的头部获取一个元素并从队列中删除该元素，队列为空时阻塞线程
        //return list.peek();//从队列的头部获取一个元素但不删除该元素，队列为空时返回null
    }

    //Delayed Queue测试 - 添加元素
    public List<String> addDelayedQueue(String a, Long b) {
        RQueue<String> queue = redissonClient.getQueue("my_blocking_queue");//目标队列
        RDelayedQueue<String> list = redissonClient.getDelayedQueue(queue);//延迟队列，数据临时存放地，发出后删除该元素
        list.offer(a, b, TimeUnit.SECONDS);
        return list.readAll();
    }

    @PostConstruct
    public void acceptElement() {
        RBlockingQueue<String> list = redissonClient.getBlockingQueue("my_blocking_queue");
        list.subscribeOnElements(s -> System.out.println("获取到元素:" + s));
    }
}

