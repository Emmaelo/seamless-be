package com.northwest.seamless.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.northwest.seamless.dtos.request.ActivateDeactivateRequest;
import com.northwest.seamless.dtos.request.HolidayRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.HRISOverviewResponse;
import com.northwest.seamless.service.interfaces.HRISService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hris")
public class HRISController {

    private final HRISService hrisService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<HRISOverviewResponse>> getHrisOverviewPageResponses() {
        return ResponseEntity.ok().body(hrisService.getHrisOverviewPageResponses());

    }

    @PatchMapping("/employees/action")
    public ResponseEntity<ApiResponse<String>> activateDeactivateEmployee(
            @RequestBody ActivateDeactivateRequest request) {
        return ResponseEntity.ok().body(hrisService.activateDeactivateEmployee(request));

    }

    @PostMapping("/access/{staffId}/{action}")
    public ResponseEntity<ApiResponse<String>> makeAdminOrSupervisor(@PathVariable String staffId,
            @PathVariable String action) {
        return ResponseEntity.ok().body(hrisService.makeAdminOrSupervisor(staffId, action));
    }

    @PostMapping("/create-holiday")
    public ResponseEntity<ApiResponse<String>> createPublicHoliday(@RequestBody HolidayRequest request) {
        return ResponseEntity.ok().body(hrisService.createPublicHoliday(request));
    }

}
