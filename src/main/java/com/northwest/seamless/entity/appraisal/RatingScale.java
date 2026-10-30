package com.northwest.seamless.entity.appraisal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class RatingScale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    private String title;

    private String description;

    private int label;

    

}
