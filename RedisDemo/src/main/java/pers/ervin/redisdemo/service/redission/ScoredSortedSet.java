package pers.ervin.redisdemo.service.redission;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.ervin.redisdemo.utils.DateUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScoredSortedSet {
    @Autowired
    private RedissonClient redissonClient;

    public String addScore(String a,Double b){
        //创建Set
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("simpleSet1");
        //设置过期时间
        boolean exists=set.isExists();
        set.addListener(new ExpiredObjectListener() {
            public void onExpired(String name) {
                System.out.println("超时事件被触发,name="+name);
                log.info("超时事件被触发,name={}",name);
            }
        });
        //添加元素
        set.addScore(a,b);
        if(!exists) {
            set.expireAt(DateUtil.addMinutes(new Date(), 2));
        }
        //获取元素在集合中的位置
        Integer index=set.revRank(a);
        //获取元素的评分
        Double score=set.getScore(a);
        log.info("size={},a={},index={},score={}",set.size(),a,index,score);

        //可以设置单一元属过期，但是不能触发对应过期事件
        RSetCache<String> map = redissonClient.getSetCache("simpleSet2");
        map.add(a,1, TimeUnit.MINUTES);
        // 可设置,但不会触发监听.
        map.addListener(new ExpiredObjectListener() {
            public void onExpired(String name) {
                log.info("entryExpiredListener超时事件被触发,event={}",name);
            }
        });

        //不能设置单一元属过期
        RSet<String> set1 = redissonClient.getSet("simpleSet3");
        set1.add(a);

        return "SUCCESS";
    }

    public String showList(String key){
        log.info("排行榜={}", key);
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        set.stream().forEach(a->{
            Integer index=set.revRank(a);//获取元素在集合中的位置
            Double score=set.getScore(a);//获取元素的评分
            log.info("size={},key={},index={},score={}", set.size(), a, index, score);
        });
        return "SUCCESS";
    }

    public String clearList(){
        long size=redissonClient.getKeys().deleteByPattern("*impl*");
        log.info("删除数量:{}",size);
        return "SUCCESS";
    }

    public String deleteAll(String pattern){
        long amount=redissonClient.getKeys().deleteByPattern(pattern);
        log.info("删除数量:{}",amount);
        return "SUCCESS";
    }
}

