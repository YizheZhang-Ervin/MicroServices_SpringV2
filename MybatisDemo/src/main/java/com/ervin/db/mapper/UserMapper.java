package com.ervin.db.mapper;

import com.ervin.db.entity.User;

public interface UserMapper{
    User findUserById(Integer id);
}

