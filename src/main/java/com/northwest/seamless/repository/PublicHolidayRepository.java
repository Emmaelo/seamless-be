package com.northwest.seamless.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.northwest.seamless.entity.hris.PublicHolidays;

@Repository 
public interface PublicHolidayRepository extends JpaRepository<PublicHolidays, Integer> {
    Optional<PublicHolidays> findByDate(LocalDate date);
    List<PublicHolidays> findByExpired(boolean expired);
}
