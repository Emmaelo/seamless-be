package com.northwest.seamless.dtos.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppraisalCycleRequest {
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String appraisalYear;

}
