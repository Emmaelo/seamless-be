package com.northwest.seamless.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.northwest.seamless.dtos.request.AppraisalCycleRequest;
import com.northwest.seamless.dtos.request.UserRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.service.interfaces.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-employee")
    public ResponseEntity<ApiResponse<String>> createEmployee(@RequestBody UserRequest request) {

        return ResponseEntity.ok().body(adminService.createEmployee(request));
    }

    @PostMapping("/create-appraisal")
    public ResponseEntity<ApiResponse<String>> createAppraisalCycle(@RequestBody AppraisalCycleRequest request) {

        return ResponseEntity.ok().body(adminService.createAppraisalcycle(request));
    }

}
