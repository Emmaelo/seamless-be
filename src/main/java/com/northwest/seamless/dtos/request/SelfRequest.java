package com.northwest.seamless.dtos.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelfRequest {
    private List<SelfAppraisalRequest> selfAppraisalRequests;
    private int performanceReviewId;
}
