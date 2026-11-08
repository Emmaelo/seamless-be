package com.northwest.seamless.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
