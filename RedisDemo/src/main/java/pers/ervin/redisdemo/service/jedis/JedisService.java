package pers.ervin.redisdemo.service.jedis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.List;
import java.util.Set;

@Service
public class JedisService implements DisposableBean {
    @Autowired
    private JedisPool jedisPool;

    public void doKey(){
        // 从Redis连接池获取Redis连接
        Jedis jedis = jedisPool.getResource();
        // 调用keys方法获取所有的key
        Set<String> keySet = jedis.keys("*");
        for (String key : keySet) {
            System.out.println(key);
        }
    }

    public void doString(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 1.添加一个string类型数据，key为pv，用于保存pv的值，初始值为0
        jedis.set("pv", "0");
        // 2.查询该key对应的数据
        System.out.println("pv:" + jedis.get("pv"));
        // 3.修改pv为1000
        jedis.set("pv", "1000");
        // 4.实现整形数据原子自增操作 +1
        jedis.incr("pv");
        // 5.实现整形该数据原子自增操作 +1000
        jedis.incrBy("pv", 1000);
        System.out.println(jedis.get("pv"));
        // 将jedis对象放回到连接池
        jedis.close();
    }

    public void doHash(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 1.	往Hash结构中添加以下商品库存
        // a)	iphone11 => 10000
        // b)	macbookpro => 9000
        jedis.hset("goods", "iphone11", "10000");
        jedis.hset("goods", "macbookpro", "9000");
        // 2.	获取Hash中所有的商品
        Set<String> goodSet = jedis.hkeys("goods");
        System.out.println("所有商品：");
        for (String good : goodSet) {
            System.out.println(good);
        }
        // 3.	新增3000个macbookpro库存
        // String storeMacBook = jedis.hget("goods", "macbookpro");
        // long longStore = Long.parseLong(storeMacBook);
        // long addStore = longStore + 3000;
        // jedis.hset("goods", "macbookpro", addStore + "");
        jedis.hincrBy("goods", "macbookpro", 3000);
        // 4.	删除整个Hash的数据
        jedis.del("goods");
        jedis.close();
    }

    public void doList(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 1.	向list的左边插入以下三个手机号码
        jedis.lpush("tel_list", "123456789", "987654321", "135792468");
        // 2.	从右边移除一个手机号码
        jedis.rpop("tel_list");
        // 3.	获取list所有的值
        List<String> telList = jedis.lrange("tel_list", 0, -1);
        for (String tel : telList) {
            System.out.println(tel);
        }
        jedis.close();
    }

    public void doSet(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 求UV就是求独立有多少个（不重复）
        // 1.	往一个set中添加页面 page1 的uv，用户user1访问一次该页面
        jedis.sadd("uv", "user1");
        // jedis.sadd("uv", "user3");
        // jedis.sadd("uv", "user1");
        // 2.	user2访问一次该页面
        jedis.sadd("uv", "user2");
        // 3.	user1再次访问一次该页面
        jedis.sadd("uv", "user1");
        // 4.	最后获取 page1的uv值
        System.out.println("uv:" + jedis.scard("uv"));
        jedis.close();
    }

    @Override
    public void destroy() {
        // 关闭连接池
        jedisPool.close();
    }
}
