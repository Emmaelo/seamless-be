package com.northwest.seamless.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.northwest.seamless.dtos.request.LoginRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.LoginResponse;
import com.northwest.seamless.service.interfaces.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(loginService.login(request));
    }

    @PostMapping ("/logout")
    public  ResponseEntity<ApiResponse<String>> logout(){
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().body(ApiResponse.success("Logged out successfully"));
    }

}
