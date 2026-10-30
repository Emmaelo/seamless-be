package com.northwest.seamless.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;
import com.northwest.seamless.service.interfaces.OtherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/other")
public class OtherController {
    private final OtherService otherService;

    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeeDetails(){
        
        return ResponseEntity.ok().body(otherService.findEmployeeDetails());

    }

}
