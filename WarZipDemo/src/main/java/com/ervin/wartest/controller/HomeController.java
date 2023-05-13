package com.ervin.wartest.controller;

import com.ervin.wartest.service.ZipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("home")
public class HomeController {

    @Resource
    ZipService zipService;

    @Resource
    HttpServletResponse response;

    @GetMapping("check")
    public String home(){
        return "OK";
    }

    @GetMapping("/dozip")
    public void doZip(){
        zipService.doZip(response);
    }
}
