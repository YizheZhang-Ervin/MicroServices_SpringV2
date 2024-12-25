package com.ervin.demo.service;

import com.ervin.demo.entity.MyNodeEntity;
import com.ervin.demo.entity.MyNodeRelation;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MyNodeService {
    MyNodeEntity add(MyNodeEntity myNodeEntity);
    MyNodeEntity getInfoById(long id);
    void delById(long id);
    void createRelation(String from,String relation, String to);
    void createRelationByName(String fromName);
    Page<MyNodeEntity> getListByPage(int current, int pageSize, String Name);
    List<String> getAllRealationTypes();
    Boolean existById(long id);
    MyNodeEntity updateById(MyNodeEntity myNodeEntity);
    List<MyNodeEntity> getRelationsByName(String name, String relation);
    Page<MyNodeRelation> getRelationsByName(int current, int pageSize, String name);
}