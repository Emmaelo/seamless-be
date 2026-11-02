package com.northwest.seamless.dtos.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class CelebrationResponse {
    private String name;
    private String jobRole;
    private String department;
    private LocalDate celebrationDate;
    private String celebrationType;
    private Integer yearsOfService;

}
