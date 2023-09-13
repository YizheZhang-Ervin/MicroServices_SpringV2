package per.ervin.redisdemo.service;

import org.springframework.stereotype.Service;
import per.ervin.redisdemo.utils.DistributedLock;
import per.ervin.redisdemo.utils.LockWorker;

import javax.annotation.Resource;

@Service
public class LockService extends DistributedLock {
    @Resource
    ConsumeService consumeService;

    public String consume(Integer goodId, Integer num, String userName) {
        LockWorker lockWorker = () -> {
            log.info("开始远程消费：userName：{};goodId:{},num:{}", userName, goodId, num);
            return consumeService.consume(goodId, num, userName);
        };

        return tryLock(lockWorker);
    }

}