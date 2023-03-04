package com.ervin.demo;

import com.ervin.demo.Entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class MongoTests {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        Book book = new Book();
        book.setId(2);
        book.setName("springboot2");
        book.setType("springboot2");
        book.setDescription("springboot2");
        mongoTemplate.save(book);
    }

    @Test
    void collectionOperate(String collectionName){
        // 创建
        mongoTemplate.createCollection(collectionName);
        // 删除
        mongoTemplate.dropCollection(collectionName);
    }

    @Test
    void find(){
        List<Book> all = mongoTemplate.findAll(Book.class);
        System.out.println(all);
    }

    @Test
    void insert(Class T){
        // mongoTemplate.save(T);
        // mongoTemplate.insert(T);
    }

    @Test
    void remove(Class T){
        // mongoTemplate.remove(new Query(), T);
    }

    @Test
    void update(){
        // mongoTemplate.updateFirst(Query.query(criteria), update, T);
        // mongoTemplate.updateMulti(Query.query(criteria), update, T);
    }

    @Test
    void count(Class T){
        // mongoTemplate.count(new Query(), T);
    }

    @Test
    void search(){
//        基础查询
//        查询全部：		   db.集合.find();
//        查第一条：		   db.集合.findOne()
//        查询指定数量文档：	db.集合.find().limit(10)					//查10条文档
//        跳过指定数量文档：	db.集合.find().skip(20)					//跳过20条文档
//        统计：			  	db.集合.count()
//        排序：				db.集合.sort({age:1})						//按age升序排序
//        投影：				db.集合名称.find(条件,{name:1,age:1})		 //仅保留name与age域
//
//        条件查询
//        基本格式：			db.集合.find({条件})
//        模糊查询：			db.集合.find({域名:/正则表达式/})		  //等同SQL中的like，比like强大，可以执行正则所有规则
//        条件比较运算：		   db.集合.find({域名:{$gt:值}})				//等同SQL中的数值比较操作，例如：name>18
//        包含查询：			db.集合.find({域名:{$in:[值1，值2]}})		//等同于SQL中的in
//        条件连接查询：		   db.集合.find({$and:[{条件1},{条件2}]})	   //等同于SQL中的and、or
    }
}
