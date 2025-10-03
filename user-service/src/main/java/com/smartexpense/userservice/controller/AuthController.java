package com.smartexpense.userservice.controller;

import com.smartexpense.userservice.dto.AuthRequest;
import com.smartexpense.userservice.dto.AuthResponse;
import com.smartexpense.userservice.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authenticationManager.authenticate(authToken); // throws if bad creds
        String token = jwtService.generateToken(req.email());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}