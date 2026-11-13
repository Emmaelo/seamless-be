package com.northwest.seamless.dtos.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.enumeration.Suggestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceObjectiveResponse {

    private String counterSignersComment;
    private String reviewerComment;
    private ReviewStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private Suggestion reviewerSuggestion;
    private Suggestion counterSignerSuggestion;
    private int employeeFinalScore;
    private int supervisorFinalScore;
    private LocalDate startDate;
    private LocalDate endDate;
    private String staffId;
    private String name;

    @Builder.Default
    private List<ObjectiveResponse> objectives = new ArrayList<>();

}
