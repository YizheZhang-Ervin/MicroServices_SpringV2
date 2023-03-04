package com.ervin.demo.Controller;

import com.ervin.demo.Service.ZipService;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("zip")
public class ZipController {

    @Resource
    ZipService zipService;

    @Resource
    HttpServletResponse response;

    @GetMapping("/dozip")
    public void doZip(){
        zipService.doZip(response);
    }
}
