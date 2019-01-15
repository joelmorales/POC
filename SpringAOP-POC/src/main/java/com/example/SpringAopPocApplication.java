package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy=true, proxyTargetClass=true)
@ComponentScan(basePackages="com.example.")
public class SpringAopPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAopPocApplication.class, args);
	}

	
}

