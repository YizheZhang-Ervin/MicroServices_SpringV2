package com.ervin.demo.controller;

import com.ervin.demo.entity.MyNodeEntity;
import com.ervin.demo.service.MyNodeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/myNode")
public class MyNodeController {

    @Resource
    MyNodeService myNodeService;

    @PostMapping("/add")
    public Object add(MyNodeEntity myNodeEntity) {
        return myNodeService.add(myNodeEntity);
    }

    @PostMapping("/delById")
    public Object delById(Long id) {
        myNodeService.delById(id);
        return "OK";
    }

    @PostMapping("/update")
    public Object updateById(MyNodeEntity myNodeEntity) {
        return myNodeService.updateById(myNodeEntity);
    }

    @GetMapping("/getInfoById")
    public Object getInfoById(Long id) {
        return myNodeService.getInfoById(id);
    }

    @GetMapping("/getAllRelationTypes")
    public Object getAllRelationTypes() {
        return myNodeService.getAllRealationTypes();
    }

    @PostMapping("/addDMRelationShip")
    public Object addDMRelationShip(String name, String relation, String to) {
        //直接指定关系: 对象名：xx-, 关系[父亲]->, 对象名：yy
        myNodeService.createRelation(name, relation, to);
        return "OK";
    }

    @GetMapping("/getRelationsByName")
    public Object getRelationsByName(String name, String relation) {
        return myNodeService.getRelationsByName(name, relation);
    }

    @GetMapping("/getListByPage")
    public Page<MyNodeEntity> getListByPage(int current, int pageSize) {
        return myNodeService.getListByPage(current,pageSize,"");
    }

    @GetMapping("/getRelations")
    public Object getRelations(int current,int pageSize,String name) {
        return myNodeService.getRelationsByName(current,pageSize,name);
    }
}

