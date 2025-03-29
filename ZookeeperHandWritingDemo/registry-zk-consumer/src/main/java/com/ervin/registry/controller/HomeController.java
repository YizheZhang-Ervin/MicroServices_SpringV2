package com.ervin.registry.controller;

import com.ervin.registry.client.CallClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("home")
public class HomeController {
    @GetMapping("check")
    public String check() {
        return "200 OK";
    }

    @Autowired
    private CallClient callClient;

    @GetMapping("/helloworld")
    public String HelloWorld() {
        return callClient.HelloWorld();
    }
}