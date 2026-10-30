package com.northwest.seamless.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewerAppraisalRequest {
    private int objectiveId;
    private int reviewerRating;
}
