package per.ervin.redisdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsumeService {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public String consume(Integer goodId, Integer num, String userName) {
        log.info("开始消费：userName：{};goodId:{},num:{}", userName, goodId, num);
        // DB：查商品信息
        log.info("消费前：goodId:{}剩余:{}", goodId, "999");
        // DB：消费购买商品
        log.info("消费结果：ret：{};", "success");
        // DB: 消费成功，保存商品信息
        log.info("保存用户消费信息");
        return "OK";
    }

}