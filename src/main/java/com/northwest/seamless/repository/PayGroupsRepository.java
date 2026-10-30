package com.northwest.seamless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.PayGroups;

@Repository 
public interface PayGroupsRepository extends JpaRepository<PayGroups, Integer> {

}
