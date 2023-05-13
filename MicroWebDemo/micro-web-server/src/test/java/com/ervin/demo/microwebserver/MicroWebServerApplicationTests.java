package com.ervin.demo.microwebserver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ervin.demo.microwebserver.entity.User;
import com.ervin.demo.microwebserver.enums.SexEnum;
import com.ervin.demo.microwebserver.mapper.UserMapper;
import com.ervin.demo.microwebserver.service.UserServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class MicroWebServerApplicationTests {

	@Autowired
	UserMapper userMapper;

	@Resource
	UserServiceImpl userService;

	@Test
	public void testSelectUser(){
		//SELECT id,name,age,email FROM user
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);

		//SELECT id,name,age,email FROM user WHERE id=?
		User user = userMapper.selectById(4L);

		//SELECT id,name,age,email FROM user WHERE id IN ( ? , ? )
		List<Long> idList = Arrays.asList(1L, 2L, 3L);
		List<User> list1 = userMapper.selectBatchIds(idList);
		list1.forEach(System.out::println);

		//SELECT id,name,age,email FROM user WHERE name = ? AND age = ?
		Map<String, Object> map = new HashMap<>();
		map.put("age", 23);
		map.put("name", "fkd");
		List<User> list2 = userMapper.selectByMap(map);
		list2.forEach(System.out::println);
	}

	@Test
	public void testInsertUser(){
		User user = new User().setId(null).setName("test").setAge(99).setEmail("test@test.test");
		//INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? )
		int result = userMapper.insert(user);
		System.out.println(result);
	}

	@Test
	public void testDeleteUser(){
		// DELETE FROM user WHERE id=?
		userMapper.deleteById(1L);

		// DELETE FROM user WHERE id IN ( ? , ? , ? )
		List<Long> idList = Arrays.asList(2L, 3L, 4L);
		int result1 = userMapper.deleteBatchIds(idList);
		System.out.println(result1);

		// DELETE FROM user WHERE name = ? AND age = ?
		Map<String, Object> map = new HashMap<>();
		map.put("age", 23);
		map.put("name", "张三");
		int result2 = userMapper.deleteByMap(map);
		System.out.println(result2);
	}

	@Test
	public void testUpdateUser(){
		User user = new User().setId(1L).setName("test").setAge(99).setEmail("test@test.test");
		// UPDATE user SET name=?, age=?, email=? WHERE id=?
		userMapper.updateById(user);
	}

	@Test
	public void testSaveBatch(){
		long count = userService.count();
		System.out.println(count);

		ArrayList<User> users = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setName("abc" + i);
			user.setAge(20 + i);
			users.add(user);
		}
		//SQL:INSERT INTO t_user (username, age) VALUES ( ?, ? )
		userService.saveBatch(users);
	}

	@Test
	public void test01(){
		QueryWrapper<User> queryWrapper =new QueryWrapper<>();
    /*SELECT id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND
         (user_name LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)*/
		queryWrapper.like("user_name", "a")
				.between("age", 20, 30)
				.isNotNull("email");
		List<User> list = userMapper.selectList(queryWrapper);
		list.forEach(System.out::println);
	}

	@Test
	public void test02(){
		//按年龄降序查询用户，如果年龄相同则按id升序排列
		//SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 ORDER BY age DESC,id ASC
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper
				.orderByDesc("age")
				.orderByAsc("id");
		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void test03(){
		//删除email为空的用户
		//DELETE FROM t_user WHERE (email IS NULL)
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.isNull("email");
		//条件构造器也可以构建删除语句的条件
		int result = userMapper.delete(queryWrapper);
		System.out.println("受影响的行数：" + result);
	}

	@Test
	public void test04() {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		//UPDATE t_user SET age=?, email=? WHERE is_deleted=0 AND (user_name LIKE ? AND age > ? OR email IS NULL)
		queryWrapper
				.like("username", "a")
				.gt("age", 20)
				.or()
				.isNull("email");
		User user = new User();
		user.setAge(18);
		user.setEmail("user@user.com");
		int result = userMapper.update(user, queryWrapper);
		System.out.println("受影响的行数：" + result);
	}

	@Test
	public void test05(){
		//将用户名中包含有a并且（年龄大于20或邮箱为null）的用户信息修改
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		//UPDATE t_user SET age=?, email=? WHERE is_deleted=0 AND (user_name LIKE ? AND (age > ? OR email IS NULL))
		queryWrapper.like("user_name", "a")
				.and(i -> i.gt("age", 20).or().isNull("email"));
		User user = new User();
		user.setEmail("test@test.com");
		user.setAge(28);
		int result = userMapper.update(user, queryWrapper);
		System.out.println(result);
	}

	@Test
	public void test06(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("user_name","age").gt("age",23);
		List<Map<String, Object>> list = userMapper.selectMaps(queryWrapper);
		list.forEach(System.out::println);
	}

	@Test
	public void test07() {
		//SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE (id IN (select id from t_user where id <= 3))
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.inSql("id", "select id from t_user where id <= 3");
		List<User> list = userMapper.selectList(queryWrapper);
		list.forEach(System.out::println);
	}

	@Test
	public void test08(){
		//将（年龄大于20或邮箱为null）并且用户名中包含有i的用户信息修改
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		//UPDATE t_user SET user_name=?,age=? WHERE is_deleted=0 AND ((age > ? OR email IS NULL) AND user_name LIKE ?)
		updateWrapper.set("user_name", "test").set("age",24)
				.and(i-> i.gt("age",20).or().isNull("email"))
				.like("user_name","i");
		userMapper.update(null, updateWrapper);
	}

	@Test
	public void testSelect(){
		//根据用户名模糊查询并且根据年龄区间查询
		String username = "w";
		Integer beginAge = 20;
		Integer endAge = 30;
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		//StringUtils.isNotBlank()判断某字符串是否不为null且长度不为0且不由空白符(whitespace)构成
		queryWrapper.like(StringUtils.isNotBlank(username),"user_name", username)
				.gt(beginAge != null, "age", beginAge)
				.lt(endAge != null, "age", endAge);
		List<User> list = userMapper.selectList(queryWrapper);
		list.forEach(System.out::println);
	}

	@Test
	public void test09() {
		//定义查询条件，有可能为null（用户未输入）
		String username = "a";
		Integer ageBegin = 10;
		Integer ageEnd = 24;
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		//避免使用字符串表示字段，防止运行时错误
		queryWrapper
				.like(StringUtils.isNotBlank(username), User::getName, username)
				.ge(ageBegin != null, User::getAge, ageBegin)
				.le(ageEnd != null, User::getAge, ageEnd);
		List<User> users = userMapper.selectList(queryWrapper);
		users.forEach(System.out::println);
	}

	@Test
	public void test10() {
		//组装set子句
		LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper
				.set(User::getAge, 18)
				.set(User::getEmail, "test@test.com")
				.like(User::getName, "a")
				.and(i -> i.lt(User::getAge, 24).or().isNull(User::getEmail));
		//lambda表达式内的逻辑优先运算
		User user = new User();
		int result = userMapper.update(user, updateWrapper);
		System.out.println("受影响的行数：" + result);
	}

	@Test
	public void testPage(){
		//传入当前页和每页记录数
		Page<User> page = new Page<>(2,3);
		userMapper.selectPage(page,null);
		//所有分页信息保存在page对象中
		System.out.println(page);
		//总记录：page.getRecords();
		//当前页：page.getCurrent();
		//每页显示的条数：page.getSize();
		//总记录数：page.getTotal();
		//总页数：page.getPages();
		//是否有上一页：page.hasPrevious();
		//是否有下一页：page.hasNext()
	}

	@Test
	public void testSelectPageVo(){
		//设置分页参数
		Page<User> page = new Page<>(1, 5);
		userMapper.selectPageVo(page, 20);
		//获取分页数据
		List<User> list = page.getRecords();
		list.forEach(System.out::println);
	}

	@Test
	public void testSexEnum(){
		//数据库中存入性别字段的值是2
		User user = new User(null,"test",24,"test@test.com", SexEnum.FEMALE,0,null);
		userMapper.insert(user);
	}

}
