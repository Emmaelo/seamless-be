package com.northwest.seamless.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data 
public class EmployeeResponse {
    private String name;
    private String workEmail;
    private String supervisor;
    private LocalDate dateOfBirth;
    private String workAnniversary;
    private String jobRole;
    private String gender;
    private String department;
    private String location;
    private String staffId;
    private String reasonForDeactivation;
    private String phoneNumber;
    private String personalEmail;
    private String payGroup;
    private String payGrade;
    private BigDecimal grossSalary;

}
