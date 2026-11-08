package com.northwest.seamless.service.implementations;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.northwest.seamless.dtos.request.AppraisalCycleRequest;
import com.northwest.seamless.dtos.request.UserRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.entity.appraisal.AppraisalCycle;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.entity.hris.KYSEntity;
import com.northwest.seamless.entity.hris.SalaryEntity;
import com.northwest.seamless.entity.hris.StaffEducation;
import com.northwest.seamless.entity.hris.Departments;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.enumeration.AccessRoles;
import com.northwest.seamless.enumeration.ReviewStatus;
import com.northwest.seamless.exceptions.ApplicationException;
import com.northwest.seamless.repository.AppraisalCycleRepository;
import com.northwest.seamless.repository.DepartmentRepository;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImpl implements AdminService {
        private final EmployeeEntityRepository employeeRepository;
        private final AppraisalCycleRepository appraisalCycleRepository;
        private final PasswordEncoder encoder;
        private final PerformanceReviewRepository performanceReviewRepository;
        private final DepartmentRepository departmentRepository;

        @Transactional
        public ApiResponse<String> createEmployee(UserRequest request) {

                if (employeeRepository.existsByWorkEmailOrPersonalEmail(request.getWorkEmail(),
                                request.getPersonalEmail())) {
                        return ApiResponse.error("Company email or Personal email already exists");
                }

                KYSEntity kys = KYSEntity.builder()
                                .staffId(request.getStaffId())
                                .maidenName(request.getMaidenName())
                                .maritalStatus(request.getMaritalStatus())
                                .religion(request.getReligion())
                                .genotype(request.getGenotype())
                                .bloodGroup(request.getBloodGroup())
                                .placeOfBirth(request.getPlaceOfBirth())
                                .spousePhoneNumber(request.getSpousePhoneNumber())
                                .numberOfChildren(request.getNumberOfChildren())
                                .ethnicity(request.getEthnicity())
                                .alternatePhoneNumber(request.getAlternatePhoneNumber())
                                .alternateEmail(request.getAlternateEmail())
                                .LGA(request.getLga())
                                .stateOfOrigin(request.getStateOfOrigin())
                                .nationality(request.getNationality())
                                .residenceAddress(request.getResidenceAddress())
                                .permanentAddress(request.getPermanentAddress())
                                .nextOfKinName(request.getNextOfKinName())
                                .nextOfKinPhoneNumber(request.getNextOfKinPhoneNumber())
                                .nextOfKinAddress(request.getNextOfKinAddress())
                                .nextOfKinRelationship(request.getNextOfKinRelationship())
                                .build();

                if (request.getEducationRecords() != null) {
                        request.getEducationRecords().forEach(educationRequest -> {

                                StaffEducation education = StaffEducation.builder()
                                                .staffId(request.getStaffId())
                                                .universityAttended(
                                                                educationRequest.getUniversityAttended())
                                                .degreeObtained(
                                                                educationRequest.getDegreeObtained())
                                                .courseOfStudy(
                                                                educationRequest.getCourseOfStudy())
                                                .yearOfGraduation(
                                                                educationRequest.getYearOfGraduation())
                                                .yearOfAdmission(
                                                                educationRequest.getYearOfAdmission())
                                                .gradeObtained(
                                                                educationRequest.getGradeObtained())
                                                .build();

                                kys.addEducationRecord(education);
                        });
                }

                SalaryEntity salary = SalaryEntity.builder()
                                .staffId(request.getStaffId())
                                .payGrade(request.getPayGrade())
                                .payGroup(request.getPayGroup())
                                .grossSalary(request.getGrossSalary())
                                .build();


                EmployeeEntity manager =  getEntity(request.getDepartment());                

                EmployeeEntity user = EmployeeEntity.builder()
                                .staffId(request.getStaffId())
                                .personalEmail(request.getPersonalEmail())
                                .workEmail(request.getWorkEmail())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .middleName(request.getMiddleName())
                                .phoneNumber(request.getPhoneNumber())
                                .gender(request.getGender())
                                .password(encoder.encode("Northwest123"))
                                .dateOfBirth(request.getDateOfBirth())
                                .dateOfEmployment(request.getDateOfEmployment())
                                .jobRole(request.getJobRole())
                                .department(request.getDepartment())
                                .contractType(request.getContractType())
                                .location(request.getLocation())
                                .bankName(request.getBankName())
                                .accountNumber(request.getAccountNumber())
                                .bvn(request.getBvn())
                                .confirmed(request.isConfirmed())
                                .taxIdentificationNumber(request.getTaxIdentificationNumber())
                                .pfaAdministrator(request.getPfaAdministrator())
                                .rsaNumber(request.getRsaNumber())

                                .managerId(manager.getStaffId())
                                .managerName(manager == null ? null : manager.getFirstName() + " " + manager.getLastName())

                                .kysEntity(kys)
                                .salaryEntity(salary)
                                .roles(request.getAccessRoles() == null
                                                || request.getAccessRoles().isEmpty()
                                                                ? new HashSet<>(Set.of(AccessRoles.USER))
                                                                : new HashSet<>(request.getAccessRoles()))
                                .build();

                employeeRepository.save(user);

                return ApiResponse.success("User created successfully");

        }

        @Transactional
        public ApiResponse<String> createAppraisalcycle(
                        AppraisalCycleRequest request) {

                if (appraisalCycleRepository.existsByAppraisalYear(
                                request.getAppraisalYear())) {
                        return ApiResponse.error("Appraisal Year already exists");
                }

                AppraisalCycle cycle = AppraisalCycle.builder()
                                .id(UUID.randomUUID().toString())
                                .title(request.getTitle())
                                .isActive(true)
                                .createdAt(LocalDateTime.now())
                                .startDate(request.getStartDate())
                                .endDate(request.getEndDate())
                                .appraisalYear(request.getAppraisalYear())
                                .build();

                cycle = appraisalCycleRepository.save(cycle);

                List<EmployeeEntity> employees = employeeRepository.findByConfirmedAndStatus(true,true);

                List<PerformanceReview> reviews = new ArrayList<>();

                for (EmployeeEntity employee : employees) {

                        PerformanceReview review = PerformanceReview.builder()
                                        .staffId(employee.getStaffId())
                                        .reviewerId(getId(employee.getDepartment(), "line"))
                                        .counterSignersId(getId(employee.getDepartment(), "hod"))
                                        .appraisalYear(request.getAppraisalYear())
                                        .status(ReviewStatus.CREATED)
                                        .appraisalCycle(cycle)
                                        .build();

                        reviews.add(review);
                }

                if (!reviews.isEmpty()) {
                        performanceReviewRepository.saveAll(reviews);
                }

                return ApiResponse.success(
                                "Appraisal Cycle Created Successfully. "
                                                + reviews.size() + " performance reviews generated.");
        }

        private String getId(String department, String identifier) {
                Departments dept = departmentRepository.findByDepartment(department)
                                .orElseThrow(() -> new ApplicationException(
                                                "Department not found: " + department));

                if ("hod".equalsIgnoreCase(identifier)) {
                        return dept.getHodStaffId();
                }
                return dept.getLineManagerStaffId();

        }

        private EmployeeEntity getEntity(String department){
                Departments dept = departmentRepository.findByDepartment(department)
                                .orElseThrow(() -> new ApplicationException(
                                                "Department not found: " + department));
        
                EmployeeEntity employeeEntity = employeeRepository.findByStaffId(dept.getLineManagerStaffId()).orElse(new EmployeeEntity());

                return employeeEntity;


        }

}
