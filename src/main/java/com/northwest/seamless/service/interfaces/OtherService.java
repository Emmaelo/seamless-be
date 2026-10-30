package com.northwest.seamless.service.interfaces;

import java.util.List;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;

public interface OtherService {
    ApiResponse<List<EmployeeResponse>> findEmployeeDetails();

}
