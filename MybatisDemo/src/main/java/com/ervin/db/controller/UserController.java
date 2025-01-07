package com.ervin.db.controller;

import com.ervin.db.entity.User;
import com.ervin.db.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/id")
    public String findUserById(@RequestParam(value = "id") int id){
        User user = userService.findUserById(id);
        if(null!=user){
            return String.format("%s,%s",user.getId(),user.getName());
        }else{
            return String.format("%s","Not Found");
        }
    }
}
