package com.northwest.seamless.service.interfaces;

import java.time.LocalDate;

import com.northwest.seamless.dtos.request.AppraisalCycleRequest;
import com.northwest.seamless.dtos.request.UserRequest;
import com.northwest.seamless.dtos.response.ApiResponse;

public interface AdminService {
    ApiResponse<String> createEmployee(UserRequest request);
    ApiResponse<String> createAppraisalcycle(AppraisalCycleRequest request);
    ApiResponse<String> deleteAppraisalCycle(String year); 
    ApiResponse<String> editAppraisalCycle(String year, LocalDate endDate);

}
