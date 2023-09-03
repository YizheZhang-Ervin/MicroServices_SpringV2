package pers.ervin.redisdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.ervin.redisdemo.service.SecKillService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    SecKillService secKillService;

    @GetMapping("/secKill")
    public Boolean secKill(@RequestParam(value = "goodId")String goodId,
                          @RequestParam(value = "userId") String userId){
        return secKillService.secKill(goodId,userId);
    }
}
