package com.ervin.db.controller;

import com.ervin.db.entity.User;
import com.ervin.db.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/id")
    public String findUserById(@RequestParam(value = "id") int id){
        User user = userService.findUserById(id);
        if(null!=user){
            return String.format("%s,%s",user.getId(),user.getName());
        }else{
            return String.format("%s","Not Found");
        }
    }

    @GetMapping("/page")
    public String selectPage(@RequestParam(value = "pageNum") int pageNum,
                            @RequestParam(value = "pageSize") int pageSize){
        PageInfo<User> userPageInfo = userService.selectPage(pageNum,pageSize);
        List<User> userList = userPageInfo.getList();
        int pageNumNew = userPageInfo.getPageNum();
        int pageSizeNew = userPageInfo.getPageSize();
        int pages = userPageInfo.getPages();
        int size = userPageInfo.getSize();
        long total = userPageInfo.getTotal();
        if(userList.size()>0){
            return String.format("%s,%s,%s,%s,%s,%s,%s",
                    pageNumNew,pageSizeNew,pages,size,total,
                    userList.size(),userList.toString());
        }else{
            return String.format("%s","Not Found");
        }
    }
}
