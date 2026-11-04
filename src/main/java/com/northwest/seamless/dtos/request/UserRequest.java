package com.northwest.seamless.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.northwest.seamless.enumeration.AccessRoles;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserRequest {
    private String staffId;
    private String personalEmail;
    private String workEmail;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;
    private String jobRole;
    private String department;
    private String contractType;
    private String location;
    private String bankName;
    private String accountNumber;
    private String bvn;
    private boolean confirmed;
    private String taxIdentificationNumber;
    private String pfaAdministrator;
    private String rsaNumber;
   
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
    private String lga;
    private String stateOfOrigin;
    private String nationality;
    private String residenceAddress;
    private String permanentAddress;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;
    private String nextOfKinAddress;
    private String nextOfKinRelationship;

    private String payGroup;
    private String payGrade;
    private BigDecimal grossSalary;
    private List<EducationRequest> educationRecords;
    private List<AccessRoles> accessRoles;

}
