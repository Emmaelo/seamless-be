package com.northwest.seamless.dtos.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCycleResponse {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String appraisalYear;
    private boolean isActive;

}
