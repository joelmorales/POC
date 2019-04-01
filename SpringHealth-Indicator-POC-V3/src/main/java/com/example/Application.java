package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.annotation.EnableJms;

@EnableAutoConfiguration
@ComponentScan(basePackages="com.example")
@EnableJms
@EnableAspectJAutoProxy(exposeProxy=true, proxyTargetClass=true)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

