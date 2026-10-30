package com.northwest.seamless.service.interfaces;

import java.util.List;

import com.northwest.seamless.dtos.request.ObjectiveRequest;
import com.northwest.seamless.dtos.request.SelfRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;

public interface EmployeeService {
    ApiResponse<String> createObjectives(List<ObjectiveRequest> requests, int performanceReviewId);
    ApiResponse<PerformanceReviewResponse> getEmployeePerformanceReview(String year);
    ApiResponse<String> appraiseSelf(SelfRequest request);
    


}
