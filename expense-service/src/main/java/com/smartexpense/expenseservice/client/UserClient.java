package com.smartexpense.expenseservice.client;

import com.smartexpense.expenseservice.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081") // this url might change when I host user-service on my old laptop
public interface UserClient {

    @GetMapping("/api/users/by-email")
    UserResponse getByEmail(@RequestParam("email") String email);

    @GetMapping("/api/users/{id}")
    UserResponse getById(@PathVariable("id") Long id);
}
