package com.ervin.demo.microwebserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@MapperScan("com.ervin.demo.microwebserver.mapper")
@EnableEurekaServer
@SpringBootApplication
public class MicroWebServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroWebServerApplication.class, args);
	}

}
