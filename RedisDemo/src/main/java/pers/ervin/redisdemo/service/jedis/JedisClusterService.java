package pers.ervin.redisdemo.service.jedis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import pers.ervin.redisdemo.condition.JedisCondition;
import redis.clients.jedis.JedisCluster;
import java.io.IOException;


@Service
@Conditional({JedisCondition.class})
public class JedisClusterService implements DisposableBean {
    @Autowired
    JedisCluster jedisCluster;

    public void doSet() throws IOException {
        jedisCluster.set("k2", "v2");
        System.out.println(jedisCluster.get("k2"));
        jedisCluster.close();
    }

    @Override
    public void destroy() throws Exception {
        jedisCluster.close();
    }
}
