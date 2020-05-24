package com.npht.springtodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SpringtodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringtodoApplication.class, args);
	}

}
