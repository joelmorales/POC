package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;

@EnableAutoConfiguration
//@SpringBootApplication(exclude = H2QuartzDataSourceConfiguration.class)
@ComponentScan(basePackages="com.example")
@EnableJms
@EnableAspectJAutoProxy(exposeProxy=true, proxyTargetClass=true)
//@EnableAutoConfiguration(exclude={H2QuartzDataSourceConfiguration.class}) 
//@ImportResource("classpath:spring/H2Configuration.xml")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

