package com.northwest.seamless.controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.AppraisalCycleResponse;
import com.northwest.seamless.dtos.response.PerformanceManagementDashboardResponse;
import com.northwest.seamless.dtos.response.PerformanceObjectiveResponse;
import com.northwest.seamless.service.interfaces.PerformanceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performance")
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AppraisalCycleResponse>>> getAppraisalList() {
        return ResponseEntity.ok().body(performanceService.getAppraisalList());
    }

    @DeleteMapping("/{year}")
    public ResponseEntity<ApiResponse<String>> deleteAppraisalCycle(@PathVariable String year) {
        return ResponseEntity.ok().body(performanceService.deleteAppraisalCycle(year));
    }

    @PutMapping("/{year}/{endDate}")
    public ResponseEntity<ApiResponse<String>> extendAppraisalCycleEndDate(@PathVariable String year,
            @PathVariable LocalDate endDate) {
        return ResponseEntity.ok().body(performanceService.extendAppraisalCycleEndDate(year, endDate));
    }

    @GetMapping("/objectives")
    public ResponseEntity<ApiResponse<PerformanceObjectiveResponse>> getEmployeePerformanceObjective(
            @RequestParam String staffId,
            @RequestParam String year) {
        return ResponseEntity.ok().body(performanceService.getEmployeePerformanceObjective(staffId, year));

    }

    @GetMapping("/{year}")
    public ResponseEntity<ApiResponse<List<PerformanceObjectiveResponse>>> getPerformanceObjectivesPerYear(
            @PathVariable String year) {

        return ResponseEntity.ok().body(performanceService.getPerformanceObjectivesPerYear(year));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<PerformanceManagementDashboardResponse>> getPerformanceManagementDashboardInfo(
            @RequestParam String year) {
        return ResponseEntity.ok().body(performanceService.getPerformanceManagementDashboardInfo(year));

    }

}
