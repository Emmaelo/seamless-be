package com.northwest.seamless.service.interfaces;

import com.northwest.seamless.dtos.request.ActivateDeactivateRequest;
import com.northwest.seamless.dtos.request.HolidayRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.HRISOverviewResponse;

public interface HRISService {
    ApiResponse<HRISOverviewResponse> getHrisOverviewPageResponses();
    ApiResponse<String> activateDeactivateEmployee(ActivateDeactivateRequest request);
    ApiResponse<String> makeAdminOrSupervisor(String staffId, String action);
    ApiResponse<String> createPublicHoliday(HolidayRequest request);

}
