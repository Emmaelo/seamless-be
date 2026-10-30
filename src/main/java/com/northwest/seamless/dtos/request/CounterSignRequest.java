package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class CounterSignRequest {
    private int performanceReviewId;
    private String comment;
    private String suggestion;

}
