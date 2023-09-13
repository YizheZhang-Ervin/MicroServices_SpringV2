package per.ervin.redisdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.ervin.redisdemo.service.LockService;
import per.ervin.redisdemo.utils.RedissonLock;

import javax.annotation.Resource;

@RestController
@RequestMapping("/lock")
public class LockController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    RedissonLock redissonLock;
    @Resource
    LockService lockService;

    // RedissonLock入口
    @GetMapping("update")
    public String update(@RequestParam(value = "userId")String userId){
        redissonLock.updateUser(userId);
        return "200 ok";
    }

    // DistributedLock入口
    @RequestMapping(value = "/consume")
    public String consume(@RequestParam("goodId") Integer goodId, @RequestParam("num") Integer num) {
        String userName = "test";
        log.info("收到消费请求：userName：{};goodId:{}", userName, goodId);
        return lockService.consume(goodId, num, userName);
    }
}
