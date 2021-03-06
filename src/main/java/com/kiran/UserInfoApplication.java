package com.kiran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@SpringBootApplication
@EnableScheduling
public class UserInfoApplication {

	public static void main(String[] args) {
		ApplicationContext appCtx  = SpringApplication.run(UserInfoApplication.class, args);
	}
}
