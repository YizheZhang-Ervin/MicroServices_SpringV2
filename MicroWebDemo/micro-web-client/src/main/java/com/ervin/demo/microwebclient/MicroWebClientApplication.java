package com.ervin.demo.microwebclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroWebClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroWebClientApplication.class, args);
	}

}
