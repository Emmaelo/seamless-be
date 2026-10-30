package com.northwest.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.appraisal.AppraisalCycle;

@Repository 
public interface AppraisalCycleRepository extends JpaRepository<AppraisalCycle, String> {
    boolean existsByAppraisalYear(String appraisalYear);

}
