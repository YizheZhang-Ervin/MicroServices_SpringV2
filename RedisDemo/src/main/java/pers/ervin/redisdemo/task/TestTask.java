package pers.ervin.redisdemo.task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TestTask implements Callable<Map<String,String>> {
    private String name;

    @Override
    public Map<String,String> call() {
        Map<String,String> result = new HashMap<>();
        String str1 = String.format("%s接收到任务:%s,当前时间:%s",Thread.currentThread().getName(),name,LocalDateTime.now().toString());
        System.out.println(str1);
        result.put("data","Call " + name);
        return result;
    }
}