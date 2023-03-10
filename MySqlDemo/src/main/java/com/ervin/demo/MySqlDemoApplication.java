package com.ervin.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ervin.demo.Mapper")
public class MySqlDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySqlDemoApplication.class, args);
	}

}
