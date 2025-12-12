package com.smartexpense.expenseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.smartexpense.expenseservice.client")
public class ExpenseserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseserviceApplication.class, args);
	}

}
