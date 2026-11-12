package com.northwest.seamless.service.implementations;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.stereotype.Service;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.HRISOverviewResponse;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.service.interfaces.HRISService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HRISimpl implements HRISService {
        private final EmployeeEntityRepository employeeEntityRepository;

        public ApiResponse<HRISOverviewResponse> getHrisOverviewPageResponses() {

                List<EmployeeEntity> employees = employeeEntityRepository.findAll();

                LocalDate today = LocalDate.now();

                // Total employees
                int overallHiredEmployees = employees.size();

                // Filter active employees
                List<EmployeeEntity> activeEmployees = employees.stream()
                                .filter(EmployeeEntity::isStatus)
                                .toList();

                int activeEmployeeCount = activeEmployees.size();

                // Average age of active employees
                double averageAge = activeEmployees.stream()
                                .filter(e -> e.getDateOfBirth() != null)
                                .mapToInt(e -> Period.between(e.getDateOfBirth(), today).getYears())
                                .average()
                                .orElse(0.0);

                // Average length of service of active employees
                double averageLOS = activeEmployees.stream()
                                .filter(e -> e.getDateOfEmployment() != null)
                                .mapToLong(e -> Period.between(e.getDateOfEmployment(), today).getYears())
                                .average()
                                .orElse(0.0);

                HRISOverviewResponse response = new HRISOverviewResponse();

                response.setOveralHiredEmployees(overallHiredEmployees);
                response.setActiveEmployees(activeEmployeeCount);
                response.setEmployeeAverageAge((int) Math.round(averageAge));
                response.setEmployeeAverageLOS((int) Math.round(averageLOS));

                return ApiResponse.success("success", response);
        }

        public ApiResponse<String> activateDeactivateEmployee(String action, String staffId) {

                EmployeeEntity employeeEntity = employeeEntityRepository.findByStaffId(staffId).orElse(null);
                if (employeeEntity == null) {
                        return ApiResponse.error("Employee ID not found");
                }

                if ("deactivate".equals(action)) {
                        employeeEntity.setStatus(false);
                        employeeEntityRepository.save(employeeEntity);
                        return ApiResponse.success("Employee with staff Id : " + staffId + " has been deactivated");
                }
                employeeEntity.setStatus(true);

                employeeEntityRepository.save(employeeEntity);
                return ApiResponse.success("Employee with staff Id : " + staffId + " has been activated");

        }
}
