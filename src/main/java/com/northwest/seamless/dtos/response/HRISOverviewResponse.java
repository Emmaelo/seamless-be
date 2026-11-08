package com.northwest.seamless.dtos.response;

import lombok.Data;

@Data 
public class HRISOverviewResponse {
    private int overalHiredEmployees;
    private int activeEmployees;
    private int employeeAverageAge;
    private int employeeAverageLOS;
    
}
