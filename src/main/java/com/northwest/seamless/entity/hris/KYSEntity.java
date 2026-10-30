package com.northwest.seamless.entity.hris;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class KYSEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private String staffId;
    private String maidenName;
    private String maritalStatus;
    private String religion;
    private String genotype;
    private String bloodGroup;
    private String placeOfBirth;
    private String spousePhoneNumber;
    private int numberOfChildren;
    private String ethnicity;
    private String alternatePhoneNumber;
    private String alternateEmail;
    private String LGA;
    private String stateOfOrigin;
    private String nationality;
    private String residenceAddress;
    private String permanentAddress;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;
    private String nextOfKinAddress;
    private String nextOfKinRelationship;

    @OneToMany( mappedBy = "kysEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<StaffEducation> educationRecords = new ArrayList<>();

    public void addEducationRecord(StaffEducation education) {
    educationRecords.add(education);
    education.setKysEntity(this);
}

}
