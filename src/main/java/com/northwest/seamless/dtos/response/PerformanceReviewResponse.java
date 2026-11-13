package com.northwest.seamless.dtos.response;

import java.util.List;

import com.northwest.seamless.enumeration.Suggestion;

import lombok.Data;

@Data
public class PerformanceReviewResponse {

    private String counterSignersComment;
    private String reviewerComment;
    private Suggestion reviewerSuggestion;
    private Suggestion counterSignerSuggestion;
    private List<ObjectiveResponse> objectives;

}
