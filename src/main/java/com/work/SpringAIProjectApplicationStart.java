package com.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan({"com.work"})
@EnableAsync
public class SpringAIProjectApplicationStart {
	public static void main(String[] args) {
		SpringApplication.run(SpringAIProjectApplicationStart.class, args);
	}

}