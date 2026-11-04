package com.northwest.seamless.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.CelebrationResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;
import com.northwest.seamless.entity.hris.Departments;
import com.northwest.seamless.entity.hris.PayGrades;
import com.northwest.seamless.service.interfaces.OtherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/other")
public class OtherController {
    private final OtherService otherService;

    @GetMapping("/get-employees")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeeDetails() {

        return ResponseEntity.ok().body(otherService.findEmployeeDetails());

    }

    @GetMapping("/get-celebrations")
    public ResponseEntity<ApiResponse<List<CelebrationResponse>>> findCelebrations(@RequestParam String type,
            @RequestParam String period) {
        return ResponseEntity.ok().body(otherService.findCelebrations(type, period));

    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Departments>>> getDepartments() {
        return ResponseEntity.ok().body(otherService.getDepartments());
    }

    @GetMapping("/pay-grades")
    public ResponseEntity<ApiResponse<List<PayGrades>>> getPayGrades() {
        return ResponseEntity.ok().body(otherService.getPayGrades());
    }

 
}
