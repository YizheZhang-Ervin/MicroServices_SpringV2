package com.ervin.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.ervin.es.entity.User;
import com.ervin.es.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(value = "*",  maxAge = 3600)

public class UserController {

    @Resource
    private UserService service;

    /**新增与修改用户*/
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public JSONObject saveUser(@RequestBody User user) {
        JSONObject output = new JSONObject();
        Map<String, Object> result = service.saveUser(user);
        output.put("msg", result);
        return output;
    }

    /**删除用户*/
    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public JSONObject deleteUser(@PathVariable("id") String id) {
        JSONObject output = new JSONObject();
        Map<String, Object> result = service.deleteUser(id);
        output.put("msg", result);
        return output;
    }


    /**条件查询*/
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public JSONObject getUsers(@RequestBody Map<String,Object> maps) throws Exception{
        JSONObject output = new JSONObject();
        List result = new ArrayList();
        if (maps != null) {
            result = service.getUsers(maps);
        } else {
            result = service.getAllUsers();
        }
        output.put("data", result);
        return output;
    }

}