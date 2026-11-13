package com.northwest.seamless.entity.appraisal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.enumeration.Suggestion;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    private String staffId;

    private String reviewerId;

    private String counterSignersId;

    private String counterSignersComment;

    private String reviewerComment;

    private LocalDateTime counterSignedAt;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    private Suggestion reviewerSuggestion;

    private Suggestion counterSignerSuggestion;

    private String appraisalYear;

    private int employeeFinalScore;

    private int supervisorFinalScore;

    private LocalDate startDate;

    private LocalDate endDate;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appraisal_cycle_id", nullable = false)
    private AppraisalCycle appraisalCycle;

    @OneToMany(mappedBy = "performanceReview", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Objectives> objectives = new ArrayList<>();

    public void addObjective(Objectives objective) {
        objectives.add(objective);
        objective.setPerformanceReview(this);
    }

}
