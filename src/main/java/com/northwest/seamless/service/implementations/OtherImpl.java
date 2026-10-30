package com.northwest.seamless.service.implementations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.EmployeeResponse;
import com.northwest.seamless.entity.hris.EmployeeEntity;
import com.northwest.seamless.repository.EmployeeEntityRepository;
import com.northwest.seamless.service.interfaces.OtherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtherImpl implements OtherService{

    private final EmployeeEntityRepository employeeEntityRepository;

    public ApiResponse<List<EmployeeResponse>> findEmployeeDetails() {

        List<EmployeeEntity> employeeEntities = employeeEntityRepository.findByConfirmedAndStatus(true, true);

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

}
