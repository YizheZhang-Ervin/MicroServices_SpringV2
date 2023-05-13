package com.ervin.demo.microwebclient.controller;

import com.ervin.demo.microwebclient.annotation.JwtAuthAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @JwtAuthAnnotation(value = "remote")
    @GetMapping("check")
    public String home(){
        return "200 OK";
    }
}
