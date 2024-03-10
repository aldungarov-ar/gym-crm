package com.spring.task.gymcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan
public class GymCRMApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymCRMApplication.class, args);
	}

}
