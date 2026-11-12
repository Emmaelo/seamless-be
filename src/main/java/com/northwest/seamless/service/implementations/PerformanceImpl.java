package com.northwest.seamless.service.implementations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.northwest.seamless.dtos.response.ApiResponse;
import com.northwest.seamless.dtos.response.AppraisalCycleResponse;
import com.northwest.seamless.dtos.response.PerformanceObjectiveResponse;
import com.northwest.seamless.entity.appraisal.AppraisalCycle;
import com.northwest.seamless.entity.appraisal.PerformanceReview;
import com.northwest.seamless.repository.AppraisalCycleRepository;
import com.northwest.seamless.repository.PerformanceReviewRepository;
import com.northwest.seamless.service.interfaces.PerformanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceImpl implements PerformanceService {
    private final AppraisalCycleRepository appraisalCycleRepository;
    private  final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper mapper;

    public ApiResponse<List<AppraisalCycleResponse>> getAppraisalList() {

        int currentYear = LocalDate.now().getYear();

        List<AppraisalCycle> appraisalCycles = appraisalCycleRepository.findByAppraisalYearBetween(
                String.valueOf(currentYear - 29),
                String.valueOf(currentYear));

        List<AppraisalCycleResponse> responses = new ArrayList<>();

        for (AppraisalCycle ap : appraisalCycles) {
            responses.add(mapper.map(ap, AppraisalCycleResponse.class));
        }
        return ApiResponse.success("success", responses);

    }

    @Transactional
    public ApiResponse<String> deleteAppraisalCycle(String year) {

        AppraisalCycle appraisalCycle = appraisalCycleRepository.findByAppraisalYear(year).orElse(null);

        if (appraisalCycle == null) {
            return ApiResponse.error(year + " doesn't exist");
        }

        if (!appraisalCycle.isActive()) {
            return ApiResponse.error(year + " appraisal cycle cannot be deleted");
        }

        appraisalCycleRepository.delete(appraisalCycle);

        return ApiResponse.success(year + " deleted");

    }

    @Transactional
    public ApiResponse<String> extendAppraisalCycleEndDate(String year, LocalDate endDate) {
        AppraisalCycle appraisalCycle = appraisalCycleRepository.findByAppraisalYear(year).orElse(null);

        if (appraisalCycle == null) {
            return ApiResponse.error(year + " doesn't exist");
        }

        if (!appraisalCycle.isActive()) {
            return ApiResponse.error(year + " appraisal cycle cannot be extended");
        }

        appraisalCycle.setEndDate(endDate);

        appraisalCycleRepository.save(appraisalCycle);

        return ApiResponse.success(year + " end date extended");

    }


    public ApiResponse<PerformanceObjectiveResponse> getEmployeePerformanceObjective(String staffId, String year){

        PerformanceReview performanceReview = performanceReviewRepository.findByAppraisalYearAndStaffId(year, staffId).orElse(null);
        if(performanceReview == null){
            return ApiResponse.error("Objectives not found");
        }

        return ApiResponse.success("success", mapper.map(performanceReview, PerformanceObjectiveResponse.class));


    }

}
