package com.ervin.server.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public String home(@RequestParam String urls){
        List<String> urlLis = Arrays.asList(urls.split(","));
        System.out.println(urlLis.get(0));
        System.out.println(urlLis.get(0).isEmpty());
        System.out.println(CollectionUtils.isEmpty(urlLis));
        System.out.println(ObjectUtils.isEmpty(urlLis));
        return urlLis.get(0);
    }

    @GetMapping("/encode")
    public static String encodePassword(@RequestParam String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(password);
//        String encodedPassword = "$2a$10$SnIXGO9mfuKrpP0TweZLTuuLlwR4.rwnOd0cjtpVOEZzY/f/xdcDq";
        String encodedPassword = "$2a$10$cdY/.o.TGBYpRcczcSU5ae3C2kfljkQLddjviHuTpSRo3tARZUQw6";
        boolean result = encoder.matches(password,encodedPassword);
        return encoder.encode(password)+ "," +result;
    }

    @GetMapping("/equal")
    public static String equalTest() {
        Boolean flag = Boolean.TRUE;
        boolean result1 = flag.equals(true);
        int result2 = flag.compareTo(true);
        boolean result3 = flag.booleanValue()==true;
        return result1+","+result2+","+result3;
    }
}
