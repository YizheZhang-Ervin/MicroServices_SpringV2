package com.ervin.demo.microwebserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ervin.demo.microwebserver.entity.Jwt;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtMapper extends BaseMapper<Jwt> {

}