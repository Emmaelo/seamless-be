package com.northwest.seamless.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.northwest.seamless.dtos.request.ObjectiveRequest;
import com.northwest.seamless.dtos.request.SelfRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;
import com.northwest.seamless.service.interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create-objective/{performanceReviewId}")
    public ResponseEntity<ApiResponse<String>> createObjectives(@RequestBody List<ObjectiveRequest> requests,
            @PathVariable int performanceReviewId) {

        return ResponseEntity.ok().body(employeeService.createObjectives(requests, performanceReviewId));

    }

    @GetMapping("/get-review/{year}")
    public ResponseEntity<ApiResponse<PerformanceReviewResponse>> getEmployeePerformanceReview(
            @PathVariable String year) {

        return ResponseEntity.ok().body(employeeService.getEmployeePerformanceReview(year));
    }

    @PostMapping("/appraise-self")
    public ResponseEntity<ApiResponse<String>> appraiseSelf( @RequestBody  SelfRequest request) {

        return ResponseEntity.ok().body(employeeService.appraiseSelf(request));
    }

}
