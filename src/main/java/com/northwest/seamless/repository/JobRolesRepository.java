package com.northwest.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.JobRoles;

@Repository 
public interface JobRolesRepository extends JpaRepository<JobRoles, Integer> {

}
