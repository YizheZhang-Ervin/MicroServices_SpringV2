package pers.ervin.redisdemo.service.redisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class redisTemplateService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String doGet(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public String doSet(String key,String value){
        redisTemplate.opsForValue().set(key, value);
        return "success";
    }
}
