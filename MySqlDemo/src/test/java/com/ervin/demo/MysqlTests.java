package com.ervin.demo;

import com.ervin.demo.Dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MysqlTests {

    @Resource
    UserDao userDao;

    @Test
    void search(){
        userDao.findAllUser();
    }
}
