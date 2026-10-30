package com.northwest.seamless.entity.appraisal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Objectives {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    private String title;

    private String target;

    private int staffSelfRating;

    private int reviewerRating;

    private String staffComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_review_id", nullable = false)
    private PerformanceReview performanceReview;

}


