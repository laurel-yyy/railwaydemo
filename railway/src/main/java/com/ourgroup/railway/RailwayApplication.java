package com.ourgroup.railway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ourgroup.railway.mapper")
public class RailwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailwayApplication.class, args);
	}

}
