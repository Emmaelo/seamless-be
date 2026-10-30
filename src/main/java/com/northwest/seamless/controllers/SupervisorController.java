package com.northwest.seamless.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.northwest.seamless.dtos.request.CounterSignRequest;
import com.northwest.seamless.dtos.request.ReviewerRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;
import com.northwest.seamless.service.interfaces.SupervisorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supervisor")
public class SupervisorController {
    private final SupervisorService supervisorService;

    @GetMapping("/get-employee-objective/{role}/{year}")
    public ResponseEntity<ApiResponse<List<PerformanceReviewResponse>>> getStaffsObjectives(@PathVariable String role,
            @PathVariable String year) {
        return ResponseEntity.ok().body(supervisorService.getStaffsObjectives(role, year));

    }

    @PostMapping("/approve-objective/{performanceReviewId}")
    public ResponseEntity<ApiResponse<String>> approveObective(@PathVariable int performanceReviewId) {

        return ResponseEntity.ok().body(supervisorService.approveObective(performanceReviewId));
    }

    @PostMapping("/reject-objective/{performanceReviewId}")
    public ResponseEntity<ApiResponse<String>> rejectObective(@PathVariable int performanceReviewId) {

        return ResponseEntity.ok().body(supervisorService.rejectObective(performanceReviewId));
    }

    @PostMapping("/appraise-staff")
    public ResponseEntity<ApiResponse<String>> appraiseStaff(@RequestBody ReviewerRequest request) {

        return ResponseEntity.ok().body(supervisorService.appraiseStaff(request));
    }

    @PostMapping("counter-sign")
    public ResponseEntity<ApiResponse<String>> counterSign(@RequestBody CounterSignRequest request) {
        return ResponseEntity.ok().body(supervisorService.counterSign(request));
    }

}
