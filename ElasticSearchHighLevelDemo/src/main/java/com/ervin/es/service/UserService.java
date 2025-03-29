package com.ervin.es.service;

import com.ervin.es.entity.User;
import com.ervin.es.dao.UserEsDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Resource
    private UserEsDao userEsDao;

    /**如果存在用户就修改，不存在就新增*/
    public Map<String, Object> saveUser(User user) {
        if (user.getId() == null) {
            //生成唯一随机id
            String id = UUID.randomUUID().toString().substring(24);
            user.setId(id);
        }
        return userEsDao.saveUser(user);
    }

    public Map<String,Object> deleteUser(String id) {
        return userEsDao.deleteUser(id);
    }

    public List getAllUsers() throws Exception{
        return userEsDao.getUsers(null);
    }

    public List getUsers(Map<String,Object> maps) throws Exception{
        return userEsDao.getUsers(maps);
    }

}
