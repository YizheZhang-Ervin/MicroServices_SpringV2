package pers.ervin.redisdemo.service.redisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.ervin.redisdemo.utils.RedisUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedisTemplateService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    // private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    public String doGet(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public String doSet(String key,String value){
        redisTemplate.opsForValue().set(key, value);
//        redisUtil.set(key, value);
        return "success";
    }

    /**
     * 添加键值
     * @param key
     * @param value
     * @returnhttp://127.0.0.1/redis/set?key=key1&value=1
     */
    public boolean set(String key, String value){
        return redisUtil.set(key, value);
    }

    /**
     * 添加List对象数据到redis中
     * @return
     */
    public boolean setList(){
        List<Object> xList = new ArrayList<>();

        xList.add("1");
        xList.add("2");
        xList.add("3");
        return redisUtil.lSetList("x", xList);
    }

    /**
     * 获取全部数据
     * @return
     */
    public Object getList(){
        return redisUtil.lGet("stu", 0, -1);
    }

    @Transactional
    public Map<String, Object> testTransactiont() {

        redisTemplate.opsForValue().set("key111", "v1");

        //设置要监控的key1
        redisTemplate.watch("key111");
        //开始事务，在exec命令执行前，全部只是进入队列
        redisTemplate.multi();
        redisTemplate.opsForValue().set("key211", "v2");
        //redisTemplate.opsForValue().increment("key1",1); //①
        //v2值应为null
        Object v2 = redisTemplate.opsForValue().get("key211");
        System.out.println("命令在队列，所以v2为:" + v2);
        redisTemplate.opsForValue().set("key311", "v3");

        Object v3 = redisTemplate.opsForValue().get("key311");
        System.out.println("命令在队列，所以v3为:" + v3);
        //执行exec(),将先判别key1是否在监控后修改过，如果是则不执行事务，否则执行事务
        redisTemplate.exec();//②
        //redisTemplate.discard();
        Object v31 = redisTemplate.opsForValue().get("key311");
        System.out.println("命令在队列，所以v3为:" + v31);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        return map;
    }
}
