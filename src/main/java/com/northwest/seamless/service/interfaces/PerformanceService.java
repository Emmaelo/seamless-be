package com.northwest.seamless.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.AppraisalCycleResponse;
import com.northwest.seamless.dtos.response.PerformanceManagementDashboardResponse;
import com.northwest.seamless.dtos.response.PerformanceObjectiveResponse;

public interface PerformanceService {
    ApiResponse<List<AppraisalCycleResponse>> getAppraisalList();
    ApiResponse<String> deleteAppraisalCycle(String year);
    ApiResponse<String> extendAppraisalCycleEndDate(String year, LocalDate endDate); 
    ApiResponse<PerformanceObjectiveResponse> getEmployeePerformanceObjective(String staffId, String year);
    ApiResponse<List<PerformanceObjectiveResponse>> getPerformanceObjectivesPerYear(String year);
    ApiResponse<PerformanceManagementDashboardResponse> getPerformanceManagementDashboardInfo(String year); 

}
