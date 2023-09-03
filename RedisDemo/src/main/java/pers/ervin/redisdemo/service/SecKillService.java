package pers.ervin.redisdemo.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;
import pers.ervin.redisdemo.utils.RedisUtil;
import pers.ervin.redisdemo.utils.RedissionUtil;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class SecKillService {
    @Resource
    RedissionUtil redissionUtil;
    @Resource
    RedisUtil redisUtil;
    final static String lockPrefix = "secKill:lock:";
    final static String stockPrefix = "secKill:stock:";

    // 把商品预先放入缓存中
    @PostConstruct
    public void init(){
        // 商品 + 数量
        List<String> goodList = Arrays.asList("1", "2", "3", "4", "5");
        for(String goodId:goodList){
            redisUtil.set(stockPrefix+goodId, 20);
        }
    }

    // 使用 Redis 的分布式锁实现秒杀商品的唯一性
    public boolean secKill(String goodId, String userId) {
        // 加锁操作
//        String requestId = UUID.randomUUID().toString();
        String lockKey = lockPrefix+goodId;
        long waitTime = 1000; // 获取锁等待时间，等待时间未获取到锁，立刻返回
        long expireTime = 3000; // 超时时间，查过锁超时时间立即释放
        boolean isSuccess = redissionUtil.tryLock(lockKey, TimeUnit.MILLISECONDS, waitTime,expireTime);
        if (isSuccess) {
            try {
                // 秒杀操作
                Object value = redisUtil.get(stockPrefix + goodId);
                if(null!=value){
                    String jsonStrValue = JSONObject.toJSONString(value);
                    int stock = JSONObject.parseObject(jsonStrValue,Integer.class);
                    if (stock > 0) {
                        redisUtil.decr(stockPrefix + goodId,1); // 减库存
                        // DB操作加订单
                        String str1 = String.format("新增订单：商品为%s，用户为%s",goodId,userId);
                        System.out.println(str1);
                        // MQ发送通知消息
                        String str2 = String.format("下单成功：商品为%s，用户为%s",goodId,userId);
                        System.out.println(str2);
                    }
                }else{
                    String str3 = String.format("缺货：商品为%s，购买失败：用户为%s",goodId,userId);
                    System.out.println(str3);
                    isSuccess = false;
                }
            } finally {
                // 释放锁操作
                redissionUtil.unlock(lockKey);
            }
        }
        return isSuccess;
    }
}
