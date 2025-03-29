package com.ervin.registry.client;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class CallClient {
    @Resource
    private TheClient theClient;

    @FeignClient(name = "myprovider")
    interface TheClient {
        @RequestMapping(path = "/home/helloworld", method = RequestMethod.GET)
        @ResponseBody
        String HelloWorld();
    }

    public String HelloWorld() {
        return theClient.HelloWorld();
    }
}
