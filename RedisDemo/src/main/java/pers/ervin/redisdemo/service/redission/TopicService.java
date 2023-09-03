package pers.ervin.redisdemo.service.redission;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class TopicService {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * //分发
     * http://127.0.0.1:8080/topic/produce?a=redis主题
     *
     * @param a
     * @return
     */
    public String produce(String a) {
        RTopic topic = redissonClient.getTopic("anyTopic");
        topic.publish(a);
        return "发送消息:" + a;
    }

    //订阅
    @PostConstruct
    public void consume() {
        RTopic topic = redissonClient.getTopic("anyTopic");//订阅指定话题
        //RPatternTopic topic=redissonClient.getPatternTopic("*any*");//指定话题表达式订阅多个话题
        topic.addListener(String.class, (charSequence, map) -> System.out.println("接收到消息:" + map));
    }
}
