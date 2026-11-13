package com.northwest.seamless.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.appraisal.PerformanceReview;

@Repository 
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
    Optional<PerformanceReview> findByIdAndStaffId(int id, String staffId);
    Optional<PerformanceReview> findByIdAndReviewerId(int id, String reviewerId);
    List<PerformanceReview> findByReviewerIdAndAppraisalYear(String reviewerId, String appraisalYear);
    List<PerformanceReview> findByCounterSignersIdAndAppraisalYear(String countersignersId, String appraisalYear);
    Optional<PerformanceReview> findByAppraisalYearAndStaffId(String appraisalYear, String staffId);
    List<PerformanceReview> findByAppraisalYear(String appraisalYear);

}
