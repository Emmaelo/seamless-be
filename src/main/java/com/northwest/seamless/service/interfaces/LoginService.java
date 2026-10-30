package com.northwest.seamless.service.interfaces;

import com.northwest.seamless.dtos.request.LoginRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.LoginResponse;

public interface LoginService {
    ApiResponse<LoginResponse> login(LoginRequest request);

}
