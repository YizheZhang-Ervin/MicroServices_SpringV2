package com.ervin.db.mapper;

import com.ervin.db.entity.User;

import java.util.List;

public interface UserMapper{
    User findUserById(Integer id);
    List<User> selectPage();
}

