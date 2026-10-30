package com.northwest.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.PayGrades;

@Repository 
public interface PayGradesRepository extends JpaRepository<PayGrades, Integer> {

}
