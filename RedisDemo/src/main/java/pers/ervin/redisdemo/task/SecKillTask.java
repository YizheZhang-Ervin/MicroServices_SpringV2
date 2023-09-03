package pers.ervin.redisdemo.task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pers.ervin.redisdemo.service.SecKillService;
import java.util.concurrent.Callable;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SecKillTask implements Callable<String> {
    String userId;
    String goodId;
    SecKillService secKillService;

    @Override
    public String call() {
        boolean result = secKillService.secKill(goodId,userId);
        String str1 = String.format("GoodId:%s,UserId:%s,Result:%s",goodId,userId, result);
        return str1;
    }
}