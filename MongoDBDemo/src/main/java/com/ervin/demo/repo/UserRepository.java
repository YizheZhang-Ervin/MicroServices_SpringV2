package com.ervin.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // 自定义查询方法
    List<User> findByName(String name);
}