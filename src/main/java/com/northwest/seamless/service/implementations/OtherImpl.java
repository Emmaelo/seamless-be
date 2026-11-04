package com.northwest.seamless.service.implementations;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.CelebrationResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;
import com.northwest.seamless.entity.hris.Departments;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.entity.hris.PayGrades;
import com.northwest.seamless.exceptions.ApplicationException;
import com.northwest.seamless.repository.DepartmentRepository;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.repository.PayGradesRepository;
import com.northwest.seamless.service.interfaces.OtherService;
import com.northwest.seamless.utils.EmployeeSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtherImpl implements OtherService {

    private final EmployeeEntityRepository employeeEntityRepository;
    private final DepartmentRepository departmentRepository;
    private final PayGradesRepository payGradesRepository;

    public ApiResponse<List<EmployeeResponse>> findEmployeeDetails() {

        List<EmployeeEntity> employeeEntities = employeeEntityRepository.findByStatus( true);

        List<EmployeeResponse> listOfResponse = new ArrayList<>();

        for (EmployeeEntity em : employeeEntities) {
            EmployeeResponse response = new EmployeeResponse();

            response.setName(em.getFirstName() + " " + em.getLastName());
            response.setWorkEmail(em.getWorkEmail());
            response.setSupervisor(em.getManagerName());
            response.setDateOfBirth(em.getDateOfBirth());
            response.setJobRole(em.getJobRole());
            response.setGender(em.getGender());
            response.setDepartment(em.getDepartment());
            response.setLocation(em.getLocation());
            response.setWorkAnniversary(formatWorkAnniversary(em.getDateOfEmployment()));

            listOfResponse.add(response);
        }

        return ApiResponse.success("success", listOfResponse);

    }

    private String formatWorkAnniversary(LocalDate employmentDate) {
        if (employmentDate == null) {
            return null;
        }

        int day = employmentDate.getDayOfMonth();

        String suffix;
        if (day >= 11 && day <= 13) {
            suffix = "th";
        } else {
            switch (day % 10) {
                case 1 -> suffix = "st";
                case 2 -> suffix = "nd";
                case 3 -> suffix = "rd";
                default -> suffix = "th";
            }
        }

        String month = employmentDate.format(
                DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));

        return day + suffix + " " + month;
    }

    public ApiResponse<List<CelebrationResponse>> findCelebrations(String type, String period) {
        LocalDate[] range = getCelebrationDateRange(period);

        LocalDate startDate = range[0];
        LocalDate endDate = range[1];

        Specification<EmployeeEntity> specification = EmployeeSpecification.celebrations(
                type,
                startDate,
                endDate);

        List<EmployeeEntity> employees = employeeEntityRepository.findAll(specification);

        List<CelebrationResponse> celebrations = new ArrayList<>();

        for (EmployeeEntity employee : employees) {

            LocalDate dateOfBirth = employee.getDateOfBirth();

            LocalDate dateOfEmployment = employee.getDateOfEmployment();

            // Check birthday
            if ("ALL".equals(type)
                    || "BIRTHDAY".equals(type)) {

                LocalDate birthdayOccurrence = findCelebrationOccurrence(
                        dateOfBirth,
                        startDate,
                        endDate);

                if (birthdayOccurrence != null) {

                    celebrations.add(
                            CelebrationResponse.builder()
                                    .name(employee.getFirstName() + " "
                                            + employee.getLastName())
                                    .jobRole(employee.getJobRole())
                                    .department(employee.getDepartment())
                                    .celebrationType("BIRTHDAY")
                                    .celebrationDate(birthdayOccurrence)
                                    .yearsOfService(null)
                                    .build());
                }
            }

            // Check work anniversary
            if ("ALL".equals(type)
                    || "WORK_ANNIVERSARY".equals(type)) {

                LocalDate anniversaryOccurrence = findCelebrationOccurrence(
                        dateOfEmployment,
                        startDate,
                        endDate);

                if (anniversaryOccurrence != null) {

                    int yearsOfService = anniversaryOccurrence.getYear()
                            - dateOfEmployment.getYear();

                    celebrations.add(
                            CelebrationResponse.builder()
                                    .name(employee.getFirstName() + " " + employee.getLastName())
                                    .jobRole(employee.getJobRole())
                                    .department(employee.getDepartment())
                                    .celebrationType("WORK_ANNIVERSARY")
                                    .celebrationDate(anniversaryOccurrence)
                                    .yearsOfService(yearsOfService)
                                    .build());
                }
            }
        }

        celebrations.sort(
                Comparator.comparing(
                        CelebrationResponse::getCelebrationDate));

        return ApiResponse.success(
                "Celebrations retrieved successfully",
                celebrations);

    }

    private LocalDate[] getCelebrationDateRange(
            String period) {

        LocalDate today = LocalDate.now(
                ZoneId.of("Africa/Lagos"));

        return switch (period.toUpperCase()) {

            case "TODAY" -> new LocalDate[] {
                    today,
                    today
            };

            case "NEXT_FIVE_DAYS" -> new LocalDate[] {
                    today,
                    today.plusDays(4)
            };

            case "THIS_WEEK" -> new LocalDate[] {
                    today.with(
                            TemporalAdjusters.previousOrSame(
                                    DayOfWeek.MONDAY)),
                    today.with(
                            TemporalAdjusters.nextOrSame(
                                    DayOfWeek.SUNDAY))
            };

            case "THIS_MONTH" -> new LocalDate[] {
                    today.withDayOfMonth(1),
                    today.withDayOfMonth(
                            today.lengthOfMonth())
            };

            default -> throw new ApplicationException(
                    "Invalid celebration period");
        };
    }

    private LocalDate findCelebrationOccurrence(
            LocalDate originalDate,
            LocalDate startDate,
            LocalDate endDate) {

        if (originalDate == null) {
            return null;
        }

        for (int year = startDate.getYear(); year <= endDate.getYear(); year++) {

            try {
                LocalDate occurrence = LocalDate.of(
                        year,
                        originalDate.getMonthValue(),
                        originalDate.getDayOfMonth());

                if (!occurrence.isBefore(startDate)
                        && !occurrence.isAfter(endDate)) {

                    return occurrence;
                }

            } catch (DateTimeException exception) {
                // For example, February 29 in a non-leap year.
                // Such a date is skipped.
            }
        }

        return null;
    }

    public ApiResponse<List<Departments>> getDepartments() {
        List<Departments> listOfDepartments = departmentRepository.findAll();
        return ApiResponse.success("success", listOfDepartments);
    }

    public ApiResponse<List<PayGrades>> getPayGrades() {
        List<PayGrades> listOfpPayGrades = payGradesRepository.findAll(Sort.by("payGrade").ascending());
        return ApiResponse.success("success", listOfpPayGrades);
    }

}
