package com.northwest.seamless.dtos.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewerRequest {
    private List<ReviewerAppraisalRequest> reviewerAppraisalRequest;
    private String suggestion;
    private int performanceReviewId;
     private String reviewerComment;

}
