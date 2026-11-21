package com.northwest.seamless.dtos.response;

import java.util.List;

import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.enumeration.Suggestion;

import lombok.Data;

@Data
public class PerformanceReviewResponse {

    private int id;
    private String counterSignersComment;
    private ReviewStatus status;
    private String name;
    private String staffId;
    private String reviewerComment;
    private Suggestion reviewerSuggestion;
    private Suggestion counterSignerSuggestion;
    private List<ObjectiveResponse> objectives;
    private List<BehaviouralResponse> assessments;

}
