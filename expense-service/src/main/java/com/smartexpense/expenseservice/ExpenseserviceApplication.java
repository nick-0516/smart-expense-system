package com.smartexpense.expenseservice;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.smartexpense.expenseservice.client")
public class ExpenseserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseserviceApplication.class, args);
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			var auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				// If principal is a UserDetails or name is email, we still need the raw token
				// read header from the incoming request
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				String header = request.getHeader("Authorization");
				if (header != null) template.header("Authorization", header);
			}
		};
	}
}
