package com.northwest.seamless.utils;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.exceptions.ApplicationException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class EmployeeSpecification {
     public static Specification<EmployeeEntity> celebrations(
            String type,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, cb) -> {

            Predicate birthday = recurringDateBetween(
                    cb,
                    root.get("dateOfBirth"),
                    startDate,
                    endDate
            );

            Predicate workAnniversary = recurringDateBetween(
                    cb,
                    root.get("dateOfEmployment"),
                    startDate,
                    endDate
            );

            return switch (type) {
                case "BIRTHDAY" -> birthday;
                case "WORK_ANNIVERSARY" -> workAnniversary;
                case "ALL" -> cb.or(birthday, workAnniversary);
                
            default -> throw new ApplicationException(
                    "Invalid celebration period" );
            };
        };
    }

    private static Predicate recurringDateBetween(
            CriteriaBuilder cb,
            Expression<LocalDate> dateField,
            LocalDate startDate,
            LocalDate endDate) {

        Expression<Integer> month =
                cb.function("month", Integer.class, dateField);

        Expression<Integer> day =
                cb.function("day", Integer.class, dateField);

        int startMonth = startDate.getMonthValue();
        int startDay = startDate.getDayOfMonth();

        int endMonth = endDate.getMonthValue();
        int endDay = endDate.getDayOfMonth();

        Predicate afterStart = cb.or(
                cb.greaterThan(month, startMonth),
                cb.and(
                        cb.equal(month, startMonth),
                        cb.greaterThanOrEqualTo(day, startDay)
                )
        );

        Predicate beforeEnd = cb.or(
                cb.lessThan(month, endMonth),
                cb.and(
                        cb.equal(month, endMonth),
                        cb.lessThanOrEqualTo(day, endDay)
                )
        );

        // Handles December to January date ranges
        if (startDate.getYear() != endDate.getYear()) {
            return cb.or(afterStart, beforeEnd);
        }

        return cb.and(afterStart, beforeEnd);
    }

}
