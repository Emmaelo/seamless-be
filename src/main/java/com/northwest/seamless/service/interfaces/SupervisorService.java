package com.northwest.seamless.service.interfaces;

import java.util.List;

import com.northwest.seamless.dtos.request.CounterSignRequest;
import com.northwest.seamless.dtos.request.ReviewerRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.PerformanceReviewResponse;

public interface SupervisorService {
    ApiResponse<List<PerformanceReviewResponse>> getStaffsObjectives(String role, String year);
    ApiResponse<String> approveObective(int performanceReviewId);
    ApiResponse<String> rejectObective(int performanceReviewId);
    ApiResponse<String> appraiseStaff(ReviewerRequest request);
    ApiResponse<String> counterSign(CounterSignRequest request);
}
