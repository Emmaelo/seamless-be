package com.northwest.seamless.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.appraisal.AppraisalCycle;

@Repository 
public interface AppraisalCycleRepository extends JpaRepository<AppraisalCycle, String> {
    boolean existsByAppraisalYear(String appraisalYear);
    Optional<AppraisalCycle> findByAppraisalYear(String appraisalYear);
    List<AppraisalCycle> findByAppraisalYearBetween(String startYear, String endYear);

}
