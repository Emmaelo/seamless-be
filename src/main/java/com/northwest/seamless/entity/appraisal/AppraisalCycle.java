package com.northwest.seamless.entity.appraisal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class AppraisalCycle {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    private String title;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(unique = true)
    private String appraisalYear;


     @OneToMany(
        mappedBy = "appraisalCycle",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<PerformanceReview> performanceReviews = new ArrayList<>();

    public void addPerformanceReview(PerformanceReview review) {
    performanceReviews.add(review);
    review.setAppraisalCycle(this);
}

    

}
