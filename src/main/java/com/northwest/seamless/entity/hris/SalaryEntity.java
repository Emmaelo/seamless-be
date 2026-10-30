package com.northwest.seamless.entity.hris;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private String staffId;

    private String payGroup;

    private String payGrade;

    private BigDecimal grossSalary;


}
