package com.northwest.seamless.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.Departments;

@Repository 
public interface DepartmentRepository extends JpaRepository < Departments, Integer> {
    Optional<Departments> findByDepartment(String department);

}
