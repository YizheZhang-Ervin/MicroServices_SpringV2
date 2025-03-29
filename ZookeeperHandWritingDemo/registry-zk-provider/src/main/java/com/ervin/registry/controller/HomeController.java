package com.ervin.registry.controller;

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

    @GetMapping("helloworld")
    public String Helloworld(){
        return "Hello World 12123";
    }
}