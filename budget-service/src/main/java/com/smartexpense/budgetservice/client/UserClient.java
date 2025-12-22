package com.smartexpense.budgetservice.client;

import com.smartexpense.budgetservice.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "user.smartexpense.in") // this url might change when I host user-service on my old laptop
public interface UserClient {
    @GetMapping("/api/users/by-email")
    UserResponse getByEmail(@RequestParam String email);
}