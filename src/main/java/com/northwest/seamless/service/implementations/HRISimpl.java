package com.northwest.seamless.service.implementations;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.northwest.seamless.dtos.request.ActivateDeactivateRequest;
import com.northwest.seamless.dtos.request.HolidayRequest;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.HRISOverviewResponse;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.entity.hris.PublicHolidays;
import com.northwest.seamless.enumeration.AccessRoles;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PublicHolidayRepository;
import com.northwest.seamless.service.interfaces.HRISService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HRISimpl implements HRISService {
        private final EmployeeEntityRepository employeeEntityRepository;
        private final PublicHolidayRepository publicHolidayRepository;

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

        public ApiResponse<String> activateDeactivateEmployee(ActivateDeactivateRequest request) {

                EmployeeEntity employeeEntity = employeeEntityRepository.findByStaffId(request.getStaffId())
                                .orElse(null);
                if (employeeEntity == null) {
                        return ApiResponse.error("Employee ID not found");
                }

                if ("deactivate".equals(request.getAction())) {
                        employeeEntity.setStatus(false);
                        employeeEntity.setReasonForDeactivation(request.getReason());
                        employeeEntityRepository.save(employeeEntity);
                        return ApiResponse.success(
                                        "Employee with staff Id : " + request.getStaffId() + " has been deactivated");
                }
                employeeEntity.setStatus(true);

                employeeEntityRepository.save(employeeEntity);
                return ApiResponse.success("Employee with staff Id : " + request.getStaffId() + " has been activated");

        }

        public ApiResponse<String> makeAdminOrSupervisor(String staffId, String action) {

                EmployeeEntity employeeEntity = employeeEntityRepository.findByStaffId(staffId).orElse(null);
                if (employeeEntity == null) {
                        return ApiResponse.error("Employee ID not found");
                }

                if ("SUPERVISOR".equalsIgnoreCase(action.toUpperCase())) {
                        if (!employeeEntity.getRoles().contains(AccessRoles.SUPERVISOR)) {
                                employeeEntity.getRoles().add(AccessRoles.SUPERVISOR);

                        }
                } else if ("ADMIN".equalsIgnoreCase(action.toUpperCase())) {
                        if (!employeeEntity.getRoles().contains(AccessRoles.ADMIN)) {
                                employeeEntity.getRoles().add(AccessRoles.ADMIN);

                        }
                }

                employeeEntityRepository.save(employeeEntity);
                return ApiResponse.success(action + " created");

        }

        

        public ApiResponse<String> createPublicHoliday(HolidayRequest request){

                Optional <PublicHolidays> publicHoliday = publicHolidayRepository.findByDate(request.getDate());

                if(publicHoliday.isEmpty()){
                        PublicHolidays holidays = new PublicHolidays();
                        holidays.setDate(request.getDate());
                        holidays.setHoliday(request.getHoliday());

                        publicHolidayRepository.save(holidays);

                        return ApiResponse.success("Holiday created");
                }

                PublicHolidays ph = publicHoliday.get();

                ph.setHoliday(request.getHoliday());

                publicHolidayRepository.save(ph);
                return ApiResponse.success("Holiday updated");

        }
}
