package com.northwest.seamless.entity.hris;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayGroups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column (nullable = false, unique = true)
    private String payGroup;

}
