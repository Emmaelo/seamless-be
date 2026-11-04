package com.northwest.seamless.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.EmployeeEntity;

@Repository 
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {
    Optional<EmployeeEntity> findByStaffId(String staffId);
    Optional<EmployeeEntity> findByWorkEmail(String workEmail);
    boolean existsByWorkEmailOrPersonalEmail(String email, String personalEmail);
    List<EmployeeEntity> findByConfirmedAndStatus(boolean confirmed, boolean status);
    List<EmployeeEntity> findByStatus(boolean status);
    
}
