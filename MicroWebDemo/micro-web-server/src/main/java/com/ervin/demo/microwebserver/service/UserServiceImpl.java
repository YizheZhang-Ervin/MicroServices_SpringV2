package com.ervin.demo.microwebserver.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ervin.demo.microwebserver.entity.User;
import com.ervin.demo.microwebserver.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
