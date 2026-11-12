package com.northwest.seamless.service.interfaces;

import java.util.List;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.CelebrationResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;
import com.northwest.seamless.entity.hris.Departments;
import com.northwest.seamless.entity.hris.PayGrades;


public interface OtherService {
    ApiResponse<List<EmployeeResponse>> findEmployeeDetails(boolean isActive);
    ApiResponse<List<CelebrationResponse>> findCelebrations(String type, String period); 
    ApiResponse<List<Departments>> getDepartments();
    ApiResponse<List<PayGrades>> getPayGrades();
   

}
