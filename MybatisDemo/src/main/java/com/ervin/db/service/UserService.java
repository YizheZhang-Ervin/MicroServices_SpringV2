package com.ervin.db.service;

import com.ervin.db.entity.User;
import com.ervin.db.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.findUserById(id);
    }
}
