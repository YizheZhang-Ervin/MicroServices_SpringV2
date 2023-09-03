package pers.ervin.redisdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import pers.ervin.redisdemo.condition.JedisCondition;
import redis.clients.jedis.*;

import java.util.HashSet;

@Configuration
public class MyJedis {

    @Bean
    @Conditional({JedisCondition.class})
    public JedisPool makeJedisPool(){
        // JedisPoolConfig配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 指定最大空闲连接为10个
        config.setMaxIdle(10);
        // 最小空闲连接5个
        config.setMinIdle(5);
        // 最大等待时间为3000毫秒
        config.setMaxWaitMillis(3000);
        // 最大连接数为50
        config.setMaxTotal(50);

        return new JedisPool(config, "node1.xx.xx", 6379);
    }

    @Bean
    @Conditional({JedisCondition.class})
    public JedisSentinelPool makeJedisSentinelPool(){
        // JedisPoolConfig配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 指定最大空闲连接为10个
        config.setMaxIdle(10);
        // 最小空闲连接5个
        config.setMinIdle(5);
        // 最大等待时间为3000毫秒
        config.setMaxWaitMillis(3000);
        // 最大连接数为50
        config.setMaxTotal(50);

        HashSet<String> sentinelSet = new HashSet<>();
        sentinelSet.add("node1.xx.xx:26379");
        sentinelSet.add("node2.xx.xx:26379");
        sentinelSet.add("node3.xx.xx:26379");
        return new JedisSentinelPool("mymaster", sentinelSet, config);
    }

    @Bean
    @Conditional({JedisCondition.class})
    public JedisCluster makeJedisCluster(){
        // JedisPoolConfig配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 指定最大空闲连接为10个
        config.setMaxIdle(10);
        // 最小空闲连接5个
        config.setMinIdle(5);
        // 最大等待时间为3000毫秒
        config.setMaxWaitMillis(3000);
        // 最大连接数为50
        config.setMaxTotal(50);

        HashSet<HostAndPort> hostAndPortSet = new HashSet<>();
        hostAndPortSet.add(new HostAndPort("node1.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node1.xx.xx", 7002));
        hostAndPortSet.add(new HostAndPort("node2.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node2.xx.xx", 7002));
        hostAndPortSet.add(new HostAndPort("node3.xx.xx", 7001));
        hostAndPortSet.add(new HostAndPort("node3.xx.xx", 7002));
        return new JedisCluster(hostAndPortSet, config);
    }
}
