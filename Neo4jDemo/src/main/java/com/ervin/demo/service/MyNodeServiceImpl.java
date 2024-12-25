package com.ervin.demo.service;


import com.ervin.demo.dao.MyNodeDao;
import com.ervin.demo.entity.MyNodeEntity;
import com.ervin.demo.entity.MyNodeRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional(value="transactionManager")
@Service
public class MyNodeServiceImpl implements MyNodeService {

    @Resource
    MyNodeDao myNodeDao;

    @Override
    public MyNodeEntity add(MyNodeEntity myNodeEntity) {
        return myNodeDao.save(myNodeEntity);
    }

    @Override
    public MyNodeEntity getInfoById(long id) {
        return myNodeDao.findById(id).get();
    }

    @Override
    public void delById(long id) {
        myNodeDao.deleteById(id);
    }

    @Override
    public void createRelation(String from, String relation, String to) {
        myNodeDao.createRelation(from, relation, to);
    }

    @Override
    public void createRelationByName(String fromName) {
        myNodeDao.createRelationByName(fromName);
    }

    @Override
    public Page<MyNodeEntity> getListByPage(int current, int pageSize, String Name) {
        Pageable pageable = PageRequest.of(current, pageSize);
        return myNodeDao.findAll(pageable);
    }

    @Override
    public List<String> getAllRealationTypes() {
        return myNodeDao.getAllRealationTypes();
    }

    @Override
    public Boolean existById(long id) {
        return myNodeDao.existsById(id);
    }

    @Override
    public MyNodeEntity updateById(MyNodeEntity myNodeEntity) {
        return myNodeDao.updateById(myNodeEntity);
    }

    @Override
    public List<MyNodeEntity> getRelationsByName(String name, String relation) {
        return myNodeDao.getRelationsByName(name, relation);
    }

    @Override
    public Page<MyNodeRelation> getRelationsByName(int current, int pageSize, String name) {
        Pageable pageable= PageRequest.of(current,pageSize);
        return myNodeDao.getRelationsByName(name,current*pageSize,pageSize,pageable);
    }


}
