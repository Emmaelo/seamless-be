package com.northwest.seamless.service.interfaces;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.HRISOverviewResponse;

public interface HRISService {
    ApiResponse<HRISOverviewResponse> getHrisOverviewPageResponses();
    ApiResponse<String> activateDeactivateEmployee(String action, String staffId);

}
