package com.ervin.db.service;

import com.ervin.db.entity.User;
import com.ervin.db.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.findUserById(id);
    }

    public PageInfo<User> selectPage(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList = userMapper.selectPage();
        PageInfo<User> userPageInfo = new PageInfo<>(userList);
        return userPageInfo;
    }
}
