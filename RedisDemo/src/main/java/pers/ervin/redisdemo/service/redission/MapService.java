package pers.ervin.redisdemo.service.redission;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MapService {
    @Autowired
    private RedissonClient redissonClient;
    //测试map集合的名称
    private final static String key = "my_test_map";


    /**
     * //初始化Listener，仅初始化一次，过期事件不一定那么及时触发，存在一定的延时
     * //注意如果触发2次,则会执行2次回调..
     */
    @PostConstruct
    public void init() {
        //redissonClient.getXXX来创建不同类型的map ,KEY为要执行监听的key
        RMapCache<String, String> map = redissonClient.getMapCache(key);
        map.addListener(new EntryExpiredListener<String, String>() {
            @Override
            public void onExpired(EntryEvent<String, String> event) {
                log.info("{}已过期,原来的值为:{},现在的值为:{}", event.getKey(), event.getOldValue(), event.getValue());
            }
        });
        log.info("{}初始化完成", key);
    }

    //存放Key-Value对
    //http://127.0.0.1:8080/map/put/?a=myKey&b=myKeyValue22&flag=true
    public String put(String a, String b, boolean flag) {
        //redissonClient.getXXX来创建不同类型的map ,KEY为要执行监听的key
        RMapCache<String, String> map = redissonClient.getMapCache(key);
        if (flag) {
            map.put(a, b, 2, TimeUnit.SECONDS);//key设置有效时间,并在实现时,触发上面的监听函数
        } else {
            map.put(a, b);
        }
        log.info("设置{}={}成功", a, b);
        return "SUCCESS";
    }

    /**
     * 遍历map中的所以元素,需要指定key
     *
     * @return
     */
    public String put() {
        RMapCache<String, String> map = redissonClient.getMapCache(key);
        map.keySet().forEach(i -> log.info("{},{}", i, map.get(i)));
        return "SUCCESS";
    }
}

