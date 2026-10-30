package com.northwest.seamless.dtos.response;

import java.util.List;

import lombok.Data;

@Data
public class PerformanceReviewResponse {

    private String counterSignersComment;
    private String reviewerComment;
    private String reviewerSuggestion;
    private String counterSignerSuggestion;
    private List<ObjectiveResponse> objectives;

}
