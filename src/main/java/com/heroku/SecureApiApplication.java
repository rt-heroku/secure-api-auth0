package com.heroku;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.heroku", "com.auth0.spring.security.api"})
//@EnableAutoConfiguration
public class SecureApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureApiApplication.class, args);
	}
}
