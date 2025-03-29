package com.ervin.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ervin.demo.Entity.User;
import com.ervin.demo.Mapper.UserMapper;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MysqlTests {
    // SQL分析：p6spy
    // 代码自动生成器：插件easycode

    @Resource
    private UserMapper userMapper;

    // 测试查询
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    //测试查询
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    //批量查询
    @Test
    public void selectBatchIds(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    //条件查询
    @Test
    public void selectByMap(){
        HashMap<String,Object> map = new HashMap<>();
        //自定义查询
        map.put("name","abc");
        map.put("age",20);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //测试MybatisPlus分页插件
    @Test
    public void testMybatisPlus_Page(){
        // 两个参数：current的值默认是1，从1开始，不是0。size是每一页的条数。
        Page<User> page = new Page<>(1, 4);
        userMapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
    }

    //测试插入
    @Test
    public void testInsert(){
        User user = new User();
        user.setName("abc");
        user.setAge(21);
        user.setEmail("xx@yy.zz");
        int insert = userMapper.insert(user);//如果没有设置id，那么会自动生成id
        System.out.println(insert);//受影响行数
        System.out.println(user);//id会自动回填
    }

    //测试更新
    @Test
    public void testUpdate(){
        User user = new User();
        //可以通过条件自动拼接动态SQL
        user.setId(5L);
        user.setName("id:5,修改过后");
        //updateById 参数是一个对象！
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    //测试删除
    @Test
    public void testDeleteById(){
        userMapper.deleteById(4L);
    }

    //批量删除
    @Test
    public void testDeleteBatchId(){
        userMapper.deleteBatchIds(Arrays.asList(1L,2L));
    }

    //通过map删除
    @Test
    public void testdeleteByMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","xiaotian");
        userMapper.deleteByMap(map);
    }

    //测试乐观锁
    @Test
    void testOptimisticLocker_success() {
        //1.查询用户信息
        User user = userMapper.selectById(1l);
        //2.修改用户信息
        user.setName("tian");
        user.setAge(21);
        //3.执行更新操作
        userMapper.updateById(user);
    }

    // 测试并发乐观锁
    @Test
    void testOptimisticLocker_failure() {
        //模拟多线程实现插队效果
        //线程1
        User user = userMapper.selectById(1l);
        user.setName("tian");
        user.setAge(21);
        //线程2
        User user2 = userMapper.selectById(1l);
        user2.setName("xiaotian");
        user2.setAge(19);
        userMapper.updateById(user2);   //在这里插队
        userMapper.updateById(user);    //如果没有乐观锁就会覆盖插队线程的值
    }

    @Test
    void WrapperTest(){
        //查询name、邮箱不为空且年龄大于等于20的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name")
                .isNotNull("email")
                .ge("age",12);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void WrapperTest2(){
        //查询姓名为abc的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name","abc");    //equals
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    void WrapperTest3(){
        //查询年龄在19-23之间的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age", 19, 23);
        Long count = userMapper.selectCount(wrapper);//查询结果数
        System.out.println(count);
    }

    // 模糊查询
    @Test
    void WrapperTest4(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .notLike("name","a")    //查询姓名中不包含a的用户
                .likeRight("email","t");   //左和右是代表%的位置 两边都要匹配则为%e%，这里是email以t开头的 t%
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    //联表查询
    @Test
    void WrapperTest5(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.inSql("id","select id from user where id < 4");
        List<Object> objects = userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);
    }

    @Test
    void WrapperTest6(){
        //通过ID进行排序
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");   //通过id升序
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }
}
