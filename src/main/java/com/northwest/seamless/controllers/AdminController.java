package com.northwest.seamless.controllers;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/{year}")
    public ResponseEntity<ApiResponse<String>> deleteAppraisalCycle(@PathVariable String year) {
        return ResponseEntity.ok().body(adminService.deleteAppraisalCycle(year));
    }

    @PatchMapping("/{year}/{endDate}")
    public ResponseEntity<ApiResponse<String>> editAppraisalCycle(@PathVariable String year,
            @PathVariable LocalDate endDate) {
        return ResponseEntity.ok().body(adminService.editAppraisalCycle(year, endDate));

    }

}
