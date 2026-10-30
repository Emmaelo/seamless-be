package com.northwest.seamless.entity.hris;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.northwest.seamless.enumeration.AccessRoles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false, unique = true)
    private String staffId;

    @Column(nullable = false)
    private String personalEmail;

    @Column(nullable = true)
    private String workEmail;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountLocked = false;

    private LocalDate dateOfBirth;

    private LocalDate  dateOfEmployment;

    private String jobRole;

    private String department;

    private String contractType;

    private String location;

    private String bankName;

    private String accountNumber;

    private String bvn;
    
    @Builder.Default
    private boolean status = true;

    private boolean confirmed;

    private String taxIdentificationNumber;
    private String pfaAdministrator;
    private String rsaNumber;

    private String managerId;
    private String managerName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "kys_entity_staff_id")
    private KYSEntity kysEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_entity_staff_id")
    private SalaryEntity salaryEntity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "staff_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<AccessRoles> roles = new HashSet<>();

}
