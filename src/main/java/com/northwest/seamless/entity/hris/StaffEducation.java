package com.northwest.seamless.entity.hris;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class StaffEducation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String staffId;
    private String universityAttended;
    private String degreeObtained;
    private String courseOfStudy;
    private String yearOfGraduation;
    private String yearOfAdmission;
    private String gradeObtained;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_entity_staff_id", nullable = false)
    private KYSEntity kysEntity;

}
