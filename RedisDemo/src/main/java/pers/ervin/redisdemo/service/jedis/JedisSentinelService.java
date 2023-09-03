package pers.ervin.redisdemo.service.jedis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import pers.ervin.redisdemo.condition.JedisCondition;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import java.util.Set;

@Service
@Conditional({JedisCondition.class})
public class JedisSentinelService implements DisposableBean {
    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    public void doOperation(){
        // 1. 要操作Redis，肯定要获取Redis连接。现在通过哨兵连接池来获取连接
        Jedis jedis = jedisSentinelPool.getResource();
        // 2. 执行keys操作
        Set<String> keySet = jedis.keys("*");
        // 3. 遍历所有key
        for (String key : keySet) {
            System.out.println(key);
        }
        // 4. 再将连接返回到连接池
        jedis.close();
    }

    @Override
    public void destroy() {
        jedisSentinelPool.close();
    }
}
