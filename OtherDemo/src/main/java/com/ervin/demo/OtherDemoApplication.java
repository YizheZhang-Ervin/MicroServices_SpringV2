package com.ervin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

// exclude防止自动连mysql和mongodb
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class})
public class OtherDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtherDemoApplication.class, args);
	}

}
